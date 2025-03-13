import React, {useState} from 'react';
import {Alert, Button, Image, Modal, Text, TouchableOpacity, View} from 'react-native';
import NaverLogin, {GetProfileResponse, NaverLoginResponse} from "@react-native-seoul/naver-login";
import {NativeStackNavigationProp} from "@react-navigation/native-stack";
import {RootStackParamList} from "../../../Types/RootStackParamList.ts";
import {useNavigation} from "@react-navigation/native";
import {NAVER_KEY, NAVER_SECRET_KEY} from "@env";
import GeneratorType1 from "../../JoinComponent/GeneratorType1.tsx";
import SnsAdditionalInfoModal from "../SnsAdditionalInfoModal.tsx";
import axiosPost from "../../../Util/AxiosUtil.ts";
import {setToken} from "../../../Util/JwtTokenUtil.ts";


// @ts-ignore
export default function NaverLoginButton({styles}) {
    const [success, setSuccessResponse] = useState<NaverLoginResponse['successResponse']>();
    const [failure, setFailureResponse] = useState<NaverLoginResponse['failureResponse']>();
    const [getProfileRes, setGetProfileRes] = useState<GetProfileResponse>();
    const [modalVisible, setModalVisible] = useState(false);


    const [passwd, setPasswd] = useState('')
    const [newPasswd, setNewPasswd] = useState('')
    const [nickName, setNickName] = useState('')
    const [BorderBottomColor, setBoardBottomColor] = useState('lightgray')

    const navigation = useNavigation<NativeStackNavigationProp<RootStackParamList>>();
    const consumerKey = NAVER_KEY;
    const consumerSecret = NAVER_SECRET_KEY;
    const appName = 'sosamoFront';
    const serviceUrlSchemeIOS = 'navertest' as string;


    // 초기화 함수
    NaverLogin.initialize({
        appName, consumerKey, consumerSecret, serviceUrlSchemeIOS, // 수정된 부분
    });

    const login = async () => {
        try {
            const response = await NaverLogin.login();
            if (response.isSuccess) {
                if (response.successResponse) {
                    setSuccessResponse(response.successResponse);

                    console.log("successResponse", response.successResponse);

                    const profileResult = await NaverLogin.getProfile(response.successResponse.accessToken);
                    setGetProfileRes(profileResult);
                    setModalVisible(true);

                } else if (response.failureResponse) {
                    // @ts-ignore
                    const {lastErrorCodeFromNaverSDK, lastErrorDescriptionFromNaverSDK} = response;

                    if (lastErrorCodeFromNaverSDK === 'user_cancel') {
                        console.log('User cancelled the login process.');
                        // 사용자가 취소했을 때 처리할 로직 추가
                    } else {
                        console.error('Login failed:', lastErrorDescriptionFromNaverSDK);
                        // 로그인 실패 시 처리할 로직 추가
                    }
                    setFailureResponse(response.failureResponse);
                }
            } else if (response.failureResponse) {
                // @ts-ignore
                const {lastErrorCodeFromNaverSDK, lastErrorDescriptionFromNaverSDK} = response;
                if (lastErrorCodeFromNaverSDK === 'user_cancel') {
                    console.log('User cancelled the login process.');
                    // 사용자가 취소했을 때 처리할 로직 추가
                } else {
                    console.error('Login failed:', response.failureResponse);
                    // 로그인 실패 시 처리할 로직 추가
                }
                setFailureResponse(response.failureResponse);
            }
        } catch (error) {
            console.error('Login error', error);
        }
    };

    const loginAxsio = () => {

        axiosPost.post('/user/join', JSON.stringify({
            email: getProfileRes?.response.email,
            passwd : passwd,
            nickName: nickName,
            ciKey: getProfileRes?.response.id,
            userType: "NAVER"
        })).then(res => {
            setToken(res.data)

            console.log("res", res.data);

            setTimeout(() => {
                setModalVisible(false);
                navigation.navigate('TabNavigation')
            }, 100);

            if (res.data) {
                Alert.alert("회원가입이 완료 되었습니다.")

            } else {
                // navigation.navigate("TabNavigation")
            }

        })
    }


    return (<>

        <TouchableOpacity style={styles.socialButton} onPress={() => login()}>
            <Image source={require('../assets/naver.png')} style={styles.icon}/>
        </TouchableOpacity>

        <SnsAdditionalInfoModal
            modalVisible={modalVisible}
            setModalVisible={setModalVisible}
            nickName={nickName}
            setNickName={setNickName}
            passwd={passwd}
            setPasswd={setPasswd}
            newPasswd={newPasswd}
            setNewPasswd={setNewPasswd}
            BorderBottomColor={BorderBottomColor}
            setBoardBottomColor={setBoardBottomColor}
            loginAxsio={loginAxsio}
        />

    </>);
};


