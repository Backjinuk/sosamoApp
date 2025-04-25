import React, {useState} from 'react';
import {TouchableOpacity, Image, Alert} from 'react-native';
import {login, logout, unlink} from '@react-native-seoul/kakao-login';
import {useNavigation} from '@react-navigation/native';
import {NativeStackNavigationProp} from '@react-navigation/native-stack';
import {RootStackParamList} from '../../../Types/RootStackParamList.ts';
import {jwtDecode, JwtPayload} from 'jwt-decode';
import axiosPost from "../../../Util/AxiosUtil.ts";
import SnsAdditionalInfoModal from "../SnsAdditionalInfoModal.tsx";
import {setToken} from "../../../Util/JwtTokenUtil.ts";

// @ts-ignore
export default function KakaoLoginButton({styles}) {
    const [result, setResult] = useState<string>('');
    const [modalVisible, setModalVisible] = useState(false);
    const [passwd, setPasswd] = useState('');
    const [newPasswd, setNewPasswd] = useState('');
    const [nickName, setNickName] = useState('');
    const [borderBottomColor, setBorderBottomColor] = useState('lightgray');
    const navigation = useNavigation<NativeStackNavigationProp<RootStackParamList>>();

    const signInWithKakao = async (): Promise<void> => {
        try {
            const token = await login();
            setResult(JSON.stringify(token));
            setModalVisible(true);
        } catch (err) {
            console.error('login err', err);
        }
    };

    const LoginAxois = () => {
        const value = jwtDecode<JwtPayload & { niickname: string }>(result);

        axiosPost.post('/user/join', JSON.stringify({
            // 일시적으로 카카오로그인시 더미 이메일 사용
            nickName: nickName,
            email: "DummyEmail@Kakao.com",
            ciKey: value.sub,
            passwd : passwd,
            joinType: 'KAKAO'
        })).then(res => {
            if (res.data) {
                Alert.alert('회원가입이 완료 되었습니다.');
                setToken(res.data['token'])
                setModalVisible(false);
                navigation.navigate('TabNavigation');
            } else {
                Alert.alert('회원가입에 실패하였습니다.');
            }
        });
    };

    const signOutWithKakao = async (): Promise<void> => {
        try {
            const message = await logout();
            setResult(message);
            console.log(message);
        } catch (err) {
            console.error('signOut error', err);
        }
    };

    const unlinkKakao = async (): Promise<void> => {
        try {
            const message = await unlink();
            setResult(message);
            console.log(message);
        } catch (err) {
            console.error('unlink error', err);
        }
    };

    return (
        <>
            <TouchableOpacity style={styles.socialButton} onPress={() => signInWithKakao()}>
                <Image source={require('../assets/kakao.png')} style={styles.icon}/>
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
