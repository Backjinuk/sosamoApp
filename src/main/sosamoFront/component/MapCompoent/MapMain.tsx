import React, {useCallback, useEffect, useMemo, useRef, useState} from 'react';
import {Button, Linking, Text, View,} from 'react-native';
import {Camera, NaverMapView, Region,} from '@mj-studio/react-native-naver-map';
import axios from 'axios';
import Config from 'react-native-config';
import Geolocation from '@react-native-community/geolocation';
import axiosPost from "../../Util/AxiosUtil.ts";
import MapSearchBar from "./MapSearchBar.tsx";
import styles from "../CommunityComponent/styles.ts";
import CommunityAddForm from "../CommunityComponent/CommunityAddForm.tsx";
import CommunityInfoView from "../CommunityComponent/CommunityInfoView.tsx";
import Markers from "../CommunityComponent/Markers.tsx";
import BottomSheet, {BottomSheetModal, BottomSheetModalProvider, BottomSheetView} from "@gorhom/bottom-sheet";

export default function MapMain() {

    const naver_map_api_client_id = Config.NAVER_MAP_API_CLIENT_ID;
    const naver_map_api_client_secret = Config.NAVER_MAP_API_CLIENT_SECRET;

    // 네이버 길찾기 실행
    const NAVER_MAP_INSTALL_LINK = 'market://details?id=com.nhn.android.nmap';

    const bottomSheetModalRef = useRef<BottomSheetModal>(null);
    const snapPoints = useMemo(() => ['23%', '50%', '90%'], []);

    const debug = false;
    const log = (message?: any, ...optionalParams: any[]) => {
        if (debug) console.log(message, ...optionalParams);
    };
    const [camera, setCamera] = useState<Camera>();
    const [keyword, setKeyword] = useState('');
    const [locations, privateSetLocations] = useState<Location[]>([]);
    const [region, setRegion] = useState<Region>();
    const [radius, setRadius] = useState(0.3);
    const [markers, setMarkers] = useState<Community[]>([])
    const [marker, setMaker] = useState<Community | null>(null);
    const [openModal, setOpenModal] = useState(false)

    const [state, setState] = useState<string>('find');
    const [dummies, setDummies] = useState<Location[]>([]);



    // 모임 위치 Search후 마킹
    useEffect(() => {
        setOpenModal(false);
        setCommuPosition();

    }, [state]);

    const setCommuPosition = async () => {
        try {
            var myPosition = await getMyPosition();
            // radius 값을 추가
            myPosition = {...myPosition, radius: radius} as { latitude: number; longitude: number; radius: number };
            axiosPost.post("/commu/getLocationBaseInquery", JSON.stringify({
                "myPosition": myPosition
            })).then((res) => {
                setMarkers(res.data);
            });

        } catch (error) {
            console.error("Error posting location:", error);
        }
    };


    //검색 좌표들
    const setLocations = (locations: any[]) => {
        log('locations : ' + locations);
        const {array, region} = analyzeLocations(locations);
        setRegion(region);
        log('region : ' + region);
        privateSetLocations(array);
        log('완료');
    };
    const [position, privateSetPosition] = useState<Position | undefined>(
        undefined,
    );
    //마커의 현재 위치
    const setPosition = async (position: Position) => {

        const newRegion = {
            latitude: position.latitude,
            longitude: position.longitude,
            latitudeDelta: randomNumber(),
            longitudeDelta: randomNumber(),
        };

        setRegion(newRegion);

        if (position.address == undefined) {
            const info = await reverseGeocoding(position);
            const nextPosition = {...position};
            nextPosition.title = '';
            // log('start');
            // log(info.results[0]);
            // log(info.results[1]);
            //도로명 주소가 없을 경우
            if (info.results[1] === undefined || info.results[1].land === undefined) {
                nextPosition.address =
                    info.results[0].region.area1.name +
                    ' ' +
                    info.results[0].region.area2.name +
                    ' ' +
                    info.results[0].region.area3.name;
                //도로명 주소가 있을 경우
            } else {
                let positionAddress =
                    info.results[1].region.area1.name +
                    ' ' +
                    info.results[1].region.area2.name +
                    ' ' +
                    info.results[1].region.area3.name +
                    ' ' +
                    info.results[1].land.name;
                positionAddress += ' ' + info.results[1].land.number1;
                if (info.results[1].land.number2 !== '') {
                    positionAddress += '-' + info.results[1].land.number2;
                }
                nextPosition.address = positionAddress;
                nextPosition.title = info.results[1].land.addition0.value;
            }
            if (nextPosition.title == '') nextPosition.title = nextPosition.address;
            privateSetPosition(nextPosition);

        } else {

            position.title = getLocationTitle(position);
            privateSetPosition(position);
        }
    };

    // 아주 작은 랜덤 숫자 리턴
    const randomNumber = () => {
        return Math.floor(Math.random() * 10) / 10000000;
    };

    // 지역 검색 결과 분석
    const analyzeLocations = (locations: any[]) => {
        const array: Location[] = [];
        let minLat = 1000;
        let maxLat = 0;
        let minLon = 1000;
        let maxLon = 0;
        locations.forEach(e => {
            if (e.latitude === undefined) {
                const address = e.address_name;
                const longitude = parseFloat(e.x);
                const latitude = parseFloat(e.y);
                const title = e.place_name;
                log(e.address_name);
                log({address, latitude, longitude, title});
                array.push({
                    address,
                    latitude,
                    longitude,
                    title,
                });
                minLat = Math.min(minLat, latitude);
                maxLat = Math.max(maxLat, latitude);
                minLon = Math.min(minLon, longitude);
                maxLon = Math.max(maxLon, longitude);
            } else {
                const address = e.address;
                const lat = e.latitude;
                const lon = e.longitude;
                log(e.address);
                log({address, latitude: lat, longitude: lon, title: e.title});
                array.push({address, latitude: lat, longitude: lon, title: e.title});
                minLat = Math.min(minLat, lat);
                maxLat = Math.max(maxLat, lat);
                minLon = Math.min(minLon, lon);
                maxLon = Math.max(maxLon, lon);
            }
        });
        const delLat = maxLat - minLat;
        const delLon = maxLon - minLon;
        const del = Math.min(delLat, delLon);
        const region: Region = {
            latitude: minLat - del,
            longitude: minLon - del,
            latitudeDelta: 2 * del + delLat,
            longitudeDelta: 2 * del + delLon,
        };
        return {array, region};
    };

    // 좌표 => 주소 api
    const reverseGeocoding = async (position: Camera | Position) => {
        if (position === undefined) return;
        const data = await axios.get(
            'https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc',
            {
                params: {
                    coords: position.longitude + ',' + position.latitude,
                    orders: 'legalcode,roadaddr,admcode',
                    output: 'json',
                },
                headers: {
                    'X-NCP-APIGW-API-KEY-ID': naver_map_api_client_id, // 여기에 네이버 개발자 센터에서 발급받은 Client ID를 입력하세요
                    'X-NCP-APIGW-API-KEY': naver_map_api_client_secret, // 여기에 네이버 개발자 센터에서 발급받은 Client Secret을 입력하세요
                },
            },
        );
        return data.data;
    };

    // 키워드로 조회
    const getLocations = async () => {
        // 근처 500m
        let data = await axios.get(
            'https://dapi.kakao.com/v2/local/search/keyword.JSON',
            {
                params: {
                    query: keyword,
                    x: camera?.longitude,
                    y: camera?.latitude,
                    radius: 500,
                    size: 5,
                    sort: 'distance',
                },
                headers: {
                    Authorization: 'KakaoAK ' + Config.KAKAO_REST_API_KEY,
                },
            },
        );
        // 전체 거리순
        if (data.data.meta.total_count === 0) {
            log('데이터가 없습니다.');
            data = await axios.get(
                'https://dapi.kakao.com/v2/local/search/keyword.JSON',
                {
                    params: {
                        query: keyword,
                        x: camera?.longitude,
                        y: camera?.latitude,
                        size: 5,
                        sort: 'distance',
                    },
                    headers: {
                        Authorization: 'KakaoAK ' + Config.KAKAO_REST_API_KEY,
                    },
                },
            );
        }
        setLocations(data.data.documents);
    };

    const findRoute = async (location: Position) => {
        const title = encodeURI(getLocationTitle(location));
        const url = `nmap://route/walk?dlat=${location.latitude}&dlng=${location.longitude}&dname=${title}&appname=com.reactnative`;
        log(url);

        if (await Linking.canOpenURL(url)) {
            await Linking.openURL(url);
        } else {
            await Linking.openURL(NAVER_MAP_INSTALL_LINK);
        }
    };

    // html 코드 제거
    const getLocationTitle = (location: Position) => {
        const title = location?.title;
        if (title === undefined) {
            const data = reverseGeocoding(location);
            log(data);
            return '';
        } else {
            return title
                .replace(/<[^>]+>/g, ' ')
                .replace(/\s+/g, ' ')
                .trim();
        }
    };

    // 모임등록
    const addMoim = () => {
        setState('add');
    };

    const viewMode = (marker: Community) => {
        setState('add');
        setMaker(marker)
    }

    // 내 위치 기반 모임 찾기
    const findMoimByMyPosition = async () => {
        log('위치기반 확인');
        const myPosition = await getMyPosition();
        const locations = await findCloseLocation(myPosition);
        setLocations(locations);
        privateSetPosition(undefined);
    };

    // 화면 위치 기반 모임 찾기
    const findMoimByCamera = async () => {
        if (camera === undefined) return;
        log('화면기반확인');
        const locations = await findCloseLocation(camera);
        setLocations(locations);
        privateSetPosition(undefined);
    };

    // 내 위도 경도 찾기
    const getMyPosition = async () => {
        const position: any = await getCurrentPositionAsync({
            enableHighAccuracy: true,
            timeout: 15000,
            maximumAge: 10000,
        });
        log(position);
        const latitude = Number(JSON.stringify(position.coords.latitude));
        const longitude = Number(JSON.stringify(position.coords.longitude));
        const myPosition = {
            latitude,
            longitude,
        };
        log(myPosition);
        return myPosition;
    };

    // 내 위치 조회 비동기화
    const getCurrentPositionAsync = (options?: any) => {
        return new Promise((resolve, reject) => {
            Geolocation.getCurrentPosition(
                position => resolve(position),
                error => reject(error),
                options,
            );
        });
    };

    // (임시) 입력받은 위치 기반 500m 더미 찾기
    const findCloseLocation = async (position: Position) => {
        const rule = 500;
        const array: Location[] = [];
        dummies.forEach(dummy => {
            if (distance(position, dummy) <= rule) {
                array.push(dummy);
            }
        });
        return array;
    };

    // (임시) 두 좌표간 거리 계산
    const distance = (start: Position, end: Position) => {
        const R = 6371; // 지구 반지름 (단위: km)
        const dLat = deg2rad(start.latitude - end.latitude);
        const dLon = deg2rad(start.longitude - end.longitude);
        const a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(deg2rad(start.latitude)) *
            Math.cos(deg2rad(end.latitude)) *
            Math.sin(dLon / 2) *
            Math.sin(dLon / 2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        const distance = R * c * 1000; // 두 지점 간의 거리 (단위: m)
        return distance;
    };

    // (임시) 라디안 구하기
    function deg2rad(deg: number) {
        return deg * (Math.PI / 180);
    }

    const initializeMap = async () => {
        log('init');
        const position = await getMyPosition();
        const round = 0.0025;
        const region = {
            latitude: position.latitude - round,
            longitude: position.longitude - round,
            latitudeDelta: 2 * round,
            longitudeDelta: 2 * round,
        };
        setRegion(region);
    };


    //  BottomSheet 함수
    const handlePresentModalPress = useCallback(() => {
        bottomSheetModalRef.current?.present();
    }, []);

    const handleClosePress = useCallback(() => {
        bottomSheetModalRef.current?.close();
    }, []);

    // callback when the BottomSheetModal changes
    const handleSheetChanges = useCallback((index: number) => {
        //console.log('handleSheetChanges', index);
    }, []);




    return (

        <View style={{flex: 1}}>

            <View style={styles.searchContainer}>
                <MapSearchBar
                    getLocations={getLocations}
                    privateSetPosition={privateSetPosition}
                    keyword={keyword}
                    setKeyword={setKeyword}
                    findMoimByMyPosition={findMoimByMyPosition}
                    findMoimByCamera={findMoimByCamera}
                />
            </View>


            <NaverMapView
                style={{flex: 1}}
                onInitialized={initializeMap}
                region={region}
                onCameraChanged={setCamera}
                onTapMap={(params) => {
                    setPosition(params);
                    // handleClosePress();
                    setOpenModal(false);
                    if(!openModal) {
                        handlePresentModalPress();
                    }
                }}
                isExtentBoundedInKorea={true}
                maxZoom={18}
                minZoom={9}
                animationDuration={500}
            >

                <Markers
                    position={position}
                    locations={locations}
                    markers={markers}
                    handlePresentModalPress={handlePresentModalPress}
                    setPosition={setPosition}
                    setOpenModal={() => {
                        setOpenModal(true)
                    }}
                    setMaker={setMaker}
                />

            </NaverMapView>


            <BottomSheetModalProvider>
                <BottomSheetModal
                    ref={bottomSheetModalRef}
                    index={0}
                    snapPoints={snapPoints}
                    onChange={handleSheetChanges}
                    backgroundComponent={({ style }) => (
                        <View
                            style={[
                                style,
                                {
                                    backgroundColor: '#fff',
                                    shadowColor: '#000',
                                    shadowOffset: { width: 0, height: 2 },
                                    shadowOpacity: 0.1,
                                    shadowRadius: 6,
                                    elevation : 10,
                                    borderRadius : 14,
                                },
                            ]}
                        />
                    )}
                >
                    {position && !openModal ? (
                        <View>
                            <Text>{position.title}</Text>
                            <Button title="등록" onPress={addMoim}/>
                            <Button title="길찾기" onPress={() => findRoute(position)}/>
                            <Button
                                title="닫기"
                                onPress={() => {
                                    privateSetPosition(undefined);
                                }}
                            />
                        </View>
                    ) : (
                        openModal && (

                            <View style={styles.InfoViewContainer}>
                                <CommunityInfoView
                                    marker={marker}
                                    viewMode={viewMode}
                                    setCommuPosition={setCommuPosition}
                                />
                            </View>

                        )
                    )}
                </BottomSheetModal>
            </BottomSheetModalProvider>


            <CommunityAddForm
                state={state}
                position={position}
                marker={marker}
                closeAddForm={() => {
                    setState('find');
                }}
                dummies={dummies}
                setDummies={setDummies}
                setCommuPosition={setCommuPosition}
            />

        </View>

    )
}
