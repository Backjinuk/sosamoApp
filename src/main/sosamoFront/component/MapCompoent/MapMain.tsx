import React, {useCallback, useEffect, useMemo, useRef, useState} from 'react';
import {Button, Linking, Text, View,} from 'react-native';
import {Camera, NaverMapView, Region,} from '@mj-studio/react-native-naver-map';
import styles from "../CommunityComponent/styles.ts";
import Geolocation from '@react-native-community/geolocation';
import {NAVER_MAP_API_CLIENT_ID, NAVER_MAP_API_CLIENT_SECRET , KAKAO_REST_API_KEY} from "@env"
import {BottomSheetModal, BottomSheetModalProvider} from '@gorhom/bottom-sheet';
import MapSearchBar from "./MapSearchBar.tsx";

export default function MapMain() {

    const naver_map_api_client_id = NAVER_MAP_API_CLIENT_ID
    const naver_map_api_client_secret = NAVER_MAP_API_CLIENT_SECRET

    // 네이버 길찾기 실행
    const NAVER_MAP_INSTALL_LINK = 'market://details?id=com.nhn.android.nmap';

    const bottomSheetModalRef = useRef<BottomSheetModal>(null);
    const snapPoints = useMemo(() => ['23%', '50%', '90%'], []);
    const [camera, setCamera] = useState<Camera>();
    const [markers, setMarkers] = useState<Community[]>([])
    const [marker, setMaker] = useState<Community | null>(null);
    const [openModal, setOpenModal] = useState(false)
    const [state, setState] = useState<string>('find');
    const [dummies, setDummies] = useState<Location[]>([]);
    const [region, setRegion] = useState<Region>();


    // 모임 위치 Search후 마킹
    useEffect(() => {
        setOpenModal(false);
    }, [state]);

    const initializeMap = async () => {
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


    // 내 위도 경도 찾기
    const getMyPosition = async () => {
        const position: any = await getCurrentPositionAsync({
            enableHighAccuracy: true,
            timeout: 15000,
            maximumAge: 10000,
        });
        const latitude = Number(JSON.stringify(position.coords.latitude));
        const longitude = Number(JSON.stringify(position.coords.longitude));
        const myPosition = {
            latitude,
            longitude,
        };
        return myPosition;
    };


    const getCurrentPositionAsync = (options?: any) => {
        return new Promise((resolve, reject) => {
            Geolocation.getCurrentPosition(
                position => resolve(position),
                error => reject(error),
                options,
            );
        });
    };




    return (

        <View style={{flex: 1}}>

            <View style={styles.searchContainer}>
                <MapSearchBar
                    // getLocations={getLocations}
                    // privateSetPosition={privateSetPosition}
                    // keyword={keyword}
                    // setKeyword={setKeyword}
                    // findMoimByMyPosition={findMoimByMyPosition}
                    // findMoimByCamera={findMoimByCamera}
                />
            </View>


            <NaverMapView
                style={{flex: 1}}
                onInitialized={initializeMap}
                region={region}
                onCameraChanged={setCamera}
                onTapMap={(params) => {
                    //setPosition(params);
                    // handleClosePress();
                    setOpenModal(false);
                    if(!openModal) {
                     //   handlePresentModalPress();
                    }
                }}
                isExtentBoundedInKorea={true}
                maxZoom={18}
                minZoom={9}
                animationDuration={500}
            >

                {/*<Markers*/}
                {/*    position={position}*/}
                {/*    locations={locations}*/}
                {/*    markers={markers}*/}
                {/*    handlePresentModalPress={handlePresentModalPress}*/}
                {/*    setPosition={setPosition}*/}
                {/*    setOpenModal={() => {*/}
                {/*        setOpenModal(true)*/}
                {/*    }}*/}
                {/*    setMaker={setMaker}*/}
                {/*/>*/}

            </NaverMapView>


            <BottomSheetModalProvider>
                <BottomSheetModal>
            {/*        ref={bottomSheetModalRef}*/}
            {/*        index={0}*/}
            {/*        snapPoints={snapPoints}*/}
            {/*        onChange={handleSheetChanges}*/}
            {/*        backgroundComponent={({ style }) => (*/}
            {/*            <View*/}
            {/*                style={[*/}
            {/*                    style,*/}
            {/*                    {*/}
            {/*                        backgroundColor: '#fff',*/}
            {/*                        shadowColor: '#000',*/}
            {/*                        shadowOffset: { width: 0, height: 2 },*/}
            {/*                        shadowOpacity: 0.1,*/}
            {/*                        shadowRadius: 6,*/}
            {/*                        elevation : 10,*/}
            {/*                        borderRadius : 14,*/}
            {/*                    },*/}
            {/*                ]}*/}
            {/*            />*/}
            {/*        )}*/}
            {/*    >*/}
            {/*        {position && !openModal ? (*/}
            {/*            <View>*/}
            {/*                <Text>{position.title}</Text>*/}
            {/*                <Button title="등록" onPress={addMoim}/>*/}
            {/*                <Button title="길찾기" onPress={() => findRoute(position)}/>*/}
            {/*                <Button*/}
            {/*                    title="닫기"*/}
            {/*                    onPress={() => {*/}
            {/*                        privateSetPosition(undefined);*/}
            {/*                    }}*/}
            {/*                />*/}
            {/*            </View>*/}
            {/*        ) : (*/}
            {/*            openModal && (*/}

            {/*                <View style={styles.InfoViewContainer}>*/}
            {/*                    <CommunityInfoView*/}
            {/*                        marker={marker}*/}
            {/*                        viewMode={viewMode}*/}
            {/*                        setCommuPosition={setCommuPosition}*/}
            {/*                    />*/}
            {/*                </View>*/}

            {/*            )*/}
            {/*        )}*/}
                </BottomSheetModal>
            </BottomSheetModalProvider>


            {/*<CommunityAddForm*/}
            {/*    state={state}*/}
            {/*    position={position}*/}
            {/*    marker={marker}*/}
            {/*    closeAddForm={() => {*/}
            {/*        setState('find');*/}
            {/*    }}*/}
            {/*    dummies={dummies}*/}
            {/*    setDummies={setDummies}*/}
            {/*    setCommuPosition={setCommuPosition}*/}
            {/*/>*/}

        </View>

    )
}
