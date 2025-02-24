import React, {useState} from 'react';
import {TouchableOpacity, Image, Alert} from 'react-native';
import Config from "react-native-config";
// import NaverLogin, {GetProfileResponse, NaverLoginResponse} from "@react-native-seoul/naver-login";
import axios from "axios";
import {NativeStackNavigationProp} from "@react-navigation/native-stack";
import {RootStackParamList} from "../../../Types/RootStackParamList.ts";
import {useNavigation} from "@react-navigation/native";
import axiosPost from "../../../Util/AxiosUtil.ts";
import {setToken} from "../../../Util/JwtTokenUtil.ts";


// @ts-ignore
export default function NaverLoginButton({ styles }){
    /*   const [success, setSuccessResponse]  = useState<NaverLoginResponse['successResponse']>();
       const [failure, setFailureResponse]  = useState<NaverLoginResponse['failureResponse']>();
       const [getProfileRes, setGetProfileRes] = useState<GetProfileResponse>();

       const navigation = useNavigation<NativeStackNavigationProp<RootStackParamList>>();
       const consumerKey = Config.NAVER_KEY as string;
       const consumerSecret = Config.NAVER_SECRET_KEY as string;
       const appName = 'MoGakCo';
       const serviceUrlSchemeIOS  = 'navertest' as string;


       // 초기화 함수
       NaverLogin.initialize({
           appName,
           consumerKey,
           consumerSecret,
           serviceUrlSchemeIOS, // 수정된 부분
       });

       const login = async () => {
           try {
               const response = await NaverLogin.login();
               if (response.isSuccess) {
                   if (response.successResponse) {
                       setSuccessResponse(response.successResponse);
                       const profileResult = await NaverLogin.getProfile(response.successResponse.accessToken);
                       setGetProfileRes(profileResult);

                       loginAxsio();

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
                       console.error('Login failed:', lastErrorDescriptionFromNaverSDK);
                       // 로그인 실패 시 처리할 로직 추가
                   }
                   setFailureResponse(response.failureResponse);
               }
           } catch (error) {
               console.error('Login error', error);
           }
       };

       const loginAxsio = () => {
           axiosPost.post(Config.API_BASE_URL + '/user/userJoin', JSON.stringify({
               userId: getProfileRes?.response.id,
               email : getProfileRes?.response.email,
               userName : getProfileRes?.response.name,
               phoneNum : getProfileRes?.response.mobile,
               userType : "Naver"
           }), {

               headers : {
                   "Content-Type" : "application/json"
               }

           }).then(res => {

               setToken(res.data)

               if(res.data['searchUser'] === 'true'){
                   Alert.alert("회원가입이 완료 되었습니다.")
               }else{
                   // navigation.navigate("TabNavigation")
               }

           })
       }*/



    return (
        <TouchableOpacity style={styles.socialButton} /*onPress={() => login()}*/>

            <Image source={require('../assets/naver.png')} style={styles.icon} />

        </TouchableOpacity>
    );
};


