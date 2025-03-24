import React, { useState } from 'react';
import { Alert, Image, TouchableOpacity } from 'react-native';
import { AccessToken, LoginManager } from 'react-native-fbsdk-next';
import axios from 'axios';
import Config from 'react-native-config';
import { useNavigation } from '@react-navigation/native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { RootStackParamList } from '../../../Types/RootStackParamList';
import { userInfo as UserInfoType } from '../../../Types/userInfo';
import axiosPost from "../../../Util/AxiosUtil.ts";
import {setToken} from "../../../Util/JwtTokenUtil.ts";
import SnsAdditionalInfoModal from "../SnsAdditionalInfoModal.tsx";

interface facebookUserInfo {
  photo : string,
  givenName : string,
  familyName : string,
  email : string,
  id : string,
}

export default function FaceBookLoginButton({ styles }: {styles : any}) {
  const [result, setResult] = useState<facebookUserInfo | null>(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [passwd, setPasswd] = useState('');
  const [newPasswd, setNewPasswd] = useState('');
  const [nickName, setNickName] = useState('');
  const [borderBottomColor, setBorderBottomColor] = useState('lightgray');
  const navigation = useNavigation<NativeStackNavigationProp<RootStackParamList>>();

  const facebookLogin = async () => {
    try {
      const result = await LoginManager.logInWithPermissions(['public_profile', 'email']);

      if (result.isCancelled) {
        console.log('Login canceled');
      } else {
        const tokenData = await AccessToken.getCurrentAccessToken();
        if (tokenData) {
          const accessTokenStr = tokenData.accessToken.toString();
          await fetchUserProfile(accessTokenStr);
        } else {
          Alert.alert('Login failed', 'Unable to get access token');
        }
      }
    } catch (error: any) {
      Alert.alert('Login error', error.message);
    }
  };

  const fetchUserProfile = async (accessToken: string) => {
    try {
      console.log('accessToken:', accessToken);
      const response = await fetch(
          `https://graph.facebook.com/me?access_token=${accessToken}&fields=id,name,email,picture`
      );
      const data = await response.json();
      setResult(data)
      setModalVisible(true);
    } catch (error: any) {
      Alert.alert('Failed to fetch user profile', error.message);
    }
  };

  const LoginAxois = () => {

    if (!result) {
      Alert.alert('로그인 정보가 없습니다.');
      return;
    }

    axiosPost.post('/user/join', JSON.stringify({
      ciKey: result.id,
      email: result.email,
      joinType: 'FACEBOOK',
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
        <TouchableOpacity style={styles.socialButton} onPress={facebookLogin}>
          <Image source={require('../assets/facebook.png')} style={styles.icon} />
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