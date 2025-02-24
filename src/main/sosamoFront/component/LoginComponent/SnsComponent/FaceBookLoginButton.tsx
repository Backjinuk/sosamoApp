import React, {useState} from 'react';
import {Alert, Image, TouchableOpacity, View} from 'react-native';
import {AccessToken, LoginButton, LoginManager} from 'react-native-fbsdk-next';
import axios from 'axios';
import Config from 'react-native-config';
import {useNavigation} from '@react-navigation/native';
import {NativeStackNavigationProp} from '@react-navigation/native-stack';
import {RootStackParamList} from '../../../Types/RootStackParamList.ts';
import {userInfo} from '../../../Types/userInfo.ts';

// @ts-ignore
export default function FaceBookLoginButton({styles}) {
  const [token, setToken] = useState('');
  const [userInfo, setUserInfo] = useState<userInfo>();
  const api = Config.API_BASE_URL;
  const navigation =
    useNavigation<NativeStackNavigationProp<RootStackParamList>>();

  const facebookLogin = async () => {
    try {
      const result = await LoginManager.logInWithPermissions([
        'public_profile',
        'email',
      ]);

      if (result.isCancelled) {
        console.log('Login canceled');
      } else {
        const tokenData = await AccessToken.getCurrentAccessToken();
        if (tokenData) {
          setToken(tokenData.accessToken.toString());
          //token값으로 회원의 프로필 조회
          await fetchUserProfile(token);
        } else {
          Alert.alert('Login failed', 'Unable to get access token');
        }
      }
    } catch (error) {
      // @ts-ignore
      Alert.alert('Login error', error.message);
    }
  };

  const fetchUserProfile = async (accessToken: string) => {
    try {
      const response = await fetch(
        `https://graph.facebook.com/me?access_token=${accessToken}&fields=id,name,email,picture`,
      );
      const data = await response.json();
      setUserInfo(data);
      LoginAxios();
    } catch (error) {
      // @ts-ignore
      Alert.alert('Failed to fetch user profile', error.message);
    }
  };

  const LoginAxios = () => {
    axios
      .post(
        api + '/user/userJoin',
        JSON.stringify({
          userId: userInfo?.userId,
          userName: userInfo?.name,
          email: userInfo?.email,
          usertype: `FaceBook`,
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
          setTimeout(() => {
            // navigation.navigate('TabNavigation');
          }, 1000);
        } else {
          // navigation.navigate('TabNavigation');
        }
      });
  };

  return (
    <TouchableOpacity
      style={styles.socialButton}
      onPress={() => facebookLogin()}>
      <Image source={require('../assets/facebook.png')} style={styles.icon} />
    </TouchableOpacity>
  );
}
