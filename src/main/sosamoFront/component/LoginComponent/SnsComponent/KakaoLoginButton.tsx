import React, {useState} from 'react';
import {TouchableOpacity, Image, Alert} from 'react-native';
import {login, logout, unlink} from '@react-native-seoul/kakao-login';
import Config from 'react-native-config';
import axios from 'axios';
import {useNavigation} from '@react-navigation/native';
import {NativeStackNavigationProp} from '@react-navigation/native-stack';
import {RootStackParamList} from '../../../Types/RootStackParamList.ts';
import {jwtDecode} from 'jwt-decode';

// @ts-ignore
export default function KakaoLoginButton({styles}) {
  const [result, setResult] = useState<string>('');
  const api = Config.API_BASE_URL;
  const navigation =
    useNavigation<NativeStackNavigationProp<RootStackParamList>>();

  const signInWithKakao = async (): Promise<void> => {
    try {
      const token = await login();

      setResult(JSON.stringify(token));
      LoginAxois();
    } catch (err) {
      console.error('login err', err);
    }
  };

  const LoginAxois = () => {
    const value = jwtDecode(result);

    axios
      .post(
        api + '/user/userJoin',
        JSON.stringify({
          userId: value.aud,
          // @ts-ignore
          userName: value.nickname,
          userType: 'Kakao',
        }),
        {
          headers: {
            'Content-Type': 'application/json',
          },
        },
      )
      .then(res => {
        if (res.data) {
          Alert.alert('회원가입이 완료 되었습니다.');
        } else {
          // navigation.navigate('TabNavigation');
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
    <TouchableOpacity
      style={styles.socialButton}
      onPress={() => signInWithKakao()}>
      <Image source={require('../assets/kakao.png')} style={styles.icon} />
    </TouchableOpacity>
  );
}
