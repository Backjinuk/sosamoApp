import {Image, TouchableOpacity} from 'react-native';
import React, {useEffect, useState} from 'react';
import {GoogleSignin} from '@react-native-google-signin/google-signin';
import auth from '@react-native-firebase/auth';
import Config from 'react-native-config';

// @ts-ignore
//보류....
export default function GoogleLoginButton({styles}) {
  const [token, setIdToken] = useState('');

  useEffect(() => {
    googleSigninConfigure();
  }, []);

  const googleSigninConfigure = () => {
    GoogleSignin.configure({
      // webClientId: Config.GOOGLE_CLIENT_ID, // Firebase 콘솔에서 가져온 웹 클라이언트 ID
      webClientId: 'asdfasdfs',
      offlineAccess: true, // 필요한 경우 오프라인 액세스 설정
      forceCodeForRefreshToken: true, // 필요한 경우 리프레시 토큰 강제 설정
      hostedDomain: '',
    });
  };

  const onGoogleButtonPress = async () => {
    try {
      await GoogleSignin.hasPlayServices({showPlayServicesUpdateDialog: true});

      // @ts-ignore
      const {idToken} = await GoogleSignin.signIn();

      const googleCredential = auth.GoogleAuthProvider.credential(idToken);

      return auth().signInWithCredential(googleCredential);
    } catch (e) {
      console.error('Error during Google Sign-In process: ', e);
    }
  };

  return (
    <TouchableOpacity
      style={styles.socialButton}
      onPress={() => onGoogleButtonPress()}>
      <Image source={require('../assets/google.png')} style={styles.icon} />
    </TouchableOpacity>
  );
}
