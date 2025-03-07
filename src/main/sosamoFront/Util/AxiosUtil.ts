import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { API_BASE_URL } from "@env";

// Axios 인스턴스 생성
const axiosPost = axios.create({
    baseURL: API_BASE_URL,
    timeout: 1000,
    headers: {
        'Content-Type': 'application/json'
    }
});

// 요청 인터셉터 설정
axiosPost.interceptors.request.use(
    async config => {
        try {
            // const accessToken = await AsyncStorage.getItem('AccessToken');
            // const refreshToken = await AsyncStorage.getItem('RefreshToken');
            //
            // if (accessToken) {
            //     config.headers['AccessToken'] = `Bearer ${accessToken}`;
            // }
            // if (refreshToken) {
            //     config.headers['RefreshToken'] = refreshToken;
            // }

            config.headers['Content-Type'] = 'application/json';

        } catch (error) {
            console.error('Error fetching token', error);
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

export default axiosPost;
