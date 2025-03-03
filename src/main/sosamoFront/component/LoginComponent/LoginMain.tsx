import {Image, Text, TouchableOpacity, View} from "react-native";
import React, {useEffect} from "react";
import createStyles from "./styles.ts";
import {useNavigation} from "@react-navigation/native";
import {NativeStackNavigationProp} from "@react-navigation/native-stack";
import {RootStackParamList} from "../../Types/RootStackParamList.ts";
import NaverLoginButton from "./SnsComponent/NaverLoginButton.tsx";
import KakaoLoginButton from "./SnsComponent/KakaoLoginButton.tsx";
import GoogleLoginButton from "./SnsComponent/GoogleLoginButton.tsx";
import FaceBookLoginButton from "./SnsComponent/FaceBookLoginButton.tsx";
import axiosPost from "../../Util/AxiosUtil.ts";
import AsyncStorage from "@react-native-async-storage/async-storage";
/*import getToken from "../../Util/JwtTokenUtil.ts";*/


export default function LoginMain(){
    const styles = createStyles();
    const navigation = useNavigation<NativeStackNavigationProp<RootStackParamList>>();

    /*useEffect(() => {
        const fetchToken = async () => {
            const token = await getToken();
            if (token) {

                axiosPost.post("/user/userLogin",
                    JSON.stringify({"userId" : "1", "passwd" : "2"})
                ).then((res)=>{

                    setToken(res.data);

                    setTimeout(() => {
                        navigation.navigate('MapMain')
                    }, 1000)
                })
            }
        };

        fetchToken();

    }, []);*/

    // @ts-ignore
    return (
        <View style={styles.container}>
            <Text style={styles.title}>MoGakCo</Text>
            <Text style={styles.subtitle}>회원가입</Text>
            <Text style={styles.description}>회원가입 방식을 선택해 주세요.</Text>

            <View style={styles.socialButtonsContainer}>
                <NaverLoginButton styles={styles} />
                <KakaoLoginButton styles={styles} />
                <GoogleLoginButton styles={styles}/>
                <FaceBookLoginButton styles={styles} />
            </View>

            <Text style={styles.orText}>또는</Text>

            <TouchableOpacity style={styles.signupButton} onPress={ () => { {navigation.navigate("Join");} }}>
                <Text style={styles.signupButtonText}>모각코로 회원가입</Text>
            </TouchableOpacity>

            <TouchableOpacity style={styles.signupButton}>
                <Text style={styles.signupButtonText}>이메일로 회원가입</Text>
            </TouchableOpacity>

            <TouchableOpacity onPress={ () => { {navigation.navigate("LoginForm");} }}>
                <Text style={styles.loginText}>이미 회원이신가요? 로그인</Text>
            </TouchableOpacity>

        </View>
    );
}