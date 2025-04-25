import React, { useEffect, useState } from 'react';
import { Alert, Image, TouchableOpacity } from 'react-native';
import NaverLogin, { GetProfileResponse, NaverLoginResponse } from "@react-native-seoul/naver-login";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import { RootStackParamList } from "../../../Types/RootStackParamList";
import { useNavigation } from "@react-navigation/native";
import { NAVER_KEY, NAVER_SECRET_KEY } from "@env";
import SnsAdditionalInfoModal from "../SnsAdditionalInfoModal";
import axiosPost from "../../../Util/AxiosUtil";
import {getToken, setToken} from "../../../Util/JwtTokenUtil";

// @ts-ignore
export default function NaverLoginButton({ styles }) {
    const [successResponse, setSuccessResponse] = useState<NaverLoginResponse['successResponse']>();
    const [failureResponse, setFailureResponse] = useState<NaverLoginResponse['failureResponse']>();
    const [profileResponse, setProfileResponse] = useState<GetProfileResponse>();
    const [modalVisible, setModalVisible] = useState(false);
    const [passwd, setPasswd] = useState('');
    const [newPasswd, setNewPasswd] = useState('');
    const [nickName, setNickName] = useState('');
    const [borderBottomColor, setBorderBottomColor] = useState('lightgray');

    const navigation = useNavigation<NativeStackNavigationProp<RootStackParamList>>();
    const consumerKey = NAVER_KEY;
    const consumerSecret = NAVER_SECRET_KEY;
    const appName = 'sosamoFront';
    const serviceUrlSchemeIOS = 'navertest';

    // NaverLogin 초기화: 컴포넌트가 마운트 될 때 한 번만 실행
    useEffect(() => {
        NaverLogin.initialize({
            appName,
            consumerKey,
            consumerSecret,
            serviceUrlSchemeIOS,
        });
    }, []);

    const handleNaverLogin = async () => {
        try {
            const response = await NaverLogin.login();
            if (response.isSuccess && response.successResponse) {
                setSuccessResponse(response.successResponse);

                const profileResult = await NaverLogin.getProfile(response.successResponse.accessToken);
                setProfileResponse(profileResult);
                setModalVisible(true);
            } else if (response.failureResponse) {
                // @ts-ignore
                const { lastErrorCodeFromNaverSDK, lastErrorDescriptionFromNaverSDK } = response;
                if (lastErrorCodeFromNaverSDK === 'user_cancel') {
                    console.log('User cancelled the login process.');
                } else {
                    console.error('Login failed:', lastErrorDescriptionFromNaverSDK);
                }
                setFailureResponse(response.failureResponse);
            }
        } catch (error) {
            console.error('Login error', error);
        }
    };

    const handleJoin = async () => {
        try {
            const payload = {
                email: profileResponse?.response.email,
                passwd: passwd,
                nickName: nickName,
                ciKey: profileResponse?.response.id,
                userType: "NAVER",
            };

            const res = await axiosPost.post('/user/join', JSON.stringify(payload));
            if (res.data) {
                Alert.alert("회원가입이 완료 되었습니다.");

                console.log('res.data', res.data);

                const token = {
                    AccessToken: res.data['token'],
                    RefreshToken: res.data['token'],
                }


                setToken(token);
                setModalVisible(false);
                navigation.navigate('TabNavigation');
            } else {
                Alert.alert("회원가입에 실패하였습니다.");
            }
        } catch (error) {
            console.error('Join error', error);
            Alert.alert("회원가입에 실패하였습니다.");
        }
    };

    return (
        <>
            <TouchableOpacity style={styles.socialButton} onPress={handleNaverLogin}>
                <Image source={require('../assets/naver.png')} style={styles.icon} />
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
                BorderBottomColor={borderBottomColor}
                setBoardBottomColor={setBorderBottomColor}
                loginAxsio={handleJoin}
            />
        </>
    );
}