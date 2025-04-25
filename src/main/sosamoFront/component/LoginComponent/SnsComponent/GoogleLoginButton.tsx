import { Alert, Image, TouchableOpacity } from 'react-native';
import React, { useEffect, useState } from 'react';
import { GoogleSignin } from '@react-native-google-signin/google-signin';
import auth from '@react-native-firebase/auth';
import { GOOGLE_CLIENT_ID } from '@env';
import UserInfo from "../../TabComponent/Tabs/UserInfo.tsx";
import {useNavigation} from "@react-navigation/native";
import {NativeStackNavigationProp} from "@react-navigation/native-stack";
import {RootStackParamList} from "../../../Types/RootStackParamList.ts";
import {jwtDecode, JwtPayload} from "jwt-decode";
import axiosPost from "../../../Util/AxiosUtil.ts";
import {setToken} from "../../../Util/JwtTokenUtil.ts";
import SnsAdditionalInfoModal from "../SnsAdditionalInfoModal.tsx";
import any = jasmine.any; // 환경변수에서 가져오기

interface googleUserInfo {
    photo : string,
    givenName : string,
    familyName : string,
    email : string,
    id : string,
}


export default function GoogleLoginButton({ styles }: { styles: any }) {
    const [result, setResult] = useState<googleUserInfo | null>(null);
    const [modalVisible, setModalVisible] = useState(false);
    const [passwd, setPasswd] = useState('');
    const [newPasswd, setNewPasswd] = useState('');
    const [nickName, setNickName] = useState('');
    const [borderBottomColor, setBorderBottomColor] = useState('lightgray');
    const navigation = useNavigation<NativeStackNavigationProp<RootStackParamList>>();

    useEffect(() => {
        googleSigninConfigure();
    }, []);

    const googleSigninConfigure = () => {
        GoogleSignin.configure({
            webClientId: GOOGLE_CLIENT_ID,
            offlineAccess: true, // 필요한 경우 오프라인 액세스 설정
            forceCodeForRefreshToken: true, // 필요한 경우 리프레시 토큰 강제 설정
            hostedDomain: '',
        });
    };

    const onGoogleButtonPress = async () => {
        try {
            await GoogleSignin.hasPlayServices({ showPlayServicesUpdateDialog: true });

            // 구글 로그인 시도 후 사용자 정보를 받아옵니다.
            const userInfoResponse = (await GoogleSignin.signIn()) as any;
            const user: googleUserInfo = userInfoResponse.data.user;

            setResult(user);
            setModalVisible(true);

        } catch (error: any) {
            console.error('Error during Google Sign-In process: ', error);
            Alert.alert('Google 로그인 오류', error.message || '알 수 없는 오류가 발생했습니다.');
        }
    };

    const LoginAxois = () => {

        if (!result) {
            Alert.alert('Google 로그인 정보가 없습니다.');
            return;
        }

        axiosPost.post('/user/join', JSON.stringify({
            ciKey: result.id,
            email: result.email,
            joinType: 'GOOGLE',
            nickName: nickName,
            passwd: passwd
        })).then(res => {
            if (res.data) {
                Alert.alert('회원가입이 완료 되었습니다.');
                setToken(res.data['token']);
                setModalVisible(false);
                navigation.navigate('TabNavigation');
            } else {
                Alert.alert('회원가입에 실패하였습니다.');
            }
        });
    };

    return (
        <>
            <TouchableOpacity style={styles.socialButton} onPress={onGoogleButtonPress}>
                <Image source={require('../assets/google.png')} style={styles.icon} />
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
                loginAxsio={LoginAxois}
            />
        </>
    );
}
