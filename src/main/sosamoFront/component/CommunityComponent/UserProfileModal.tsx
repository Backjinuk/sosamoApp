import React, {useEffect, useState} from 'react'
import {Image, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import {SafeAreaView} from 'react-native-safe-area-context';
import Modal from 'react-native-modal';
import {userInfo} from "../../Types/userInfo.ts";
import {useNavigation} from "@react-navigation/native";
import {NativeStackNavigationProp} from "@react-navigation/native-stack";
import {RootStackParamList} from "../../Types/RootStackParamList.ts";
import styles from "./styles.ts";
import axiosPost from "../../Util/AxiosUtil.ts";

export default function UserProfileModal(props: any) {

    const [userInfo, setUserInfo] = useState<userInfo>();
    const navigation = useNavigation<NativeStackNavigationProp<RootStackParamList>>();
    const buttonBackgroundColor = props?.subscribe?.subscribeStatus === 'Y' ? 'red' : 'blue';

    useEffect(() => {
        setUserInfo(props.commuWrite);
    }, [])

    if (!userInfo) {
        return null;
    }


    const addSubscribe = () => {
        axiosPost.post("/subscribe/addSubscribe", JSON.stringify({
            'subscriberOwnerUserSeq': userInfo.userSeq, 'subscribeStatus': 'Y'
        })).then(r => console.log(11))
    }



    return (<SafeAreaView style={styles.UserProfileModalContainer}>
        <Modal
            isVisible={props.openModal}
            animationIn="slideInUp"
            animationOut="slideOutDown"
            style={styles.modal}
            onBackdropPress={() => {
                props.setOpenModal(false)
            }}
        >
            <View style={styles.modalContent}>
                <Image
                    source={{uri: 'https://via.placeholder.com/150'}} // 여기에 실제 프로필 이미지 URL을 사용하세요
                    style={styles.profileImage}
                />
                <Text style={styles.userName}>{userInfo?.userId}</Text>
                <Text style={styles.userRole}>{userInfo?.email}</Text>
                <Text style={styles.userRole}>평점 : 0.0 / 4.5</Text>

                <View style={styles.UserProfileModalButtonContainer}>
                    <TouchableOpacity style={[styles.followButton, { backgroundColor: buttonBackgroundColor }]} onPress={() => addSubscribe()}>
                        <Text style={styles.UserProfileModalButtonText}>구독</Text>
                    </TouchableOpacity>

                    <TouchableOpacity style={styles.messageButton} onPress={() => {
                        navigation.navigate('ChatScreen');
                    }}>
                        <Text style={styles.UserProfileModalButtonText}>메시지</Text>
                    </TouchableOpacity>
                </View>

                <View style={styles.socialIcons}>
                    <Icon name="twitter" size={20} color="#1DA1F2" style={styles.icon}/>
                    <Icon name="facebook" size={20} color="#4267B2" style={styles.icon}/>
                    <Icon name="behance" size={20} color="#0057FF" style={styles.icon}/>
                    <Icon name="dribbble" size={20} color="#EA4C89" style={styles.icon}/>
                    <Icon name="close" size={20} color="#EA4C89" style={styles.icon} onPress={() => {
                        props.setOpenModal(false)
                    }}/>
                </View>
            </View>
        </Modal>
    </SafeAreaView>

    );
};
