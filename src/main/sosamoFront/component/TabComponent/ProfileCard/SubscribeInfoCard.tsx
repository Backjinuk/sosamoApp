import React from 'react';
import {View, Text, Image, StyleSheet, TouchableOpacity} from 'react-native';
import styles from "./styles.ts";
import axiosPost from "../../../Util/AxiosUtil.ts";



export default function SubscribeInfoCard(props: any) {

    const buttonBackgroundColor = props?.subscribe?.subscribeStatus === 'Y' ? 'red' : 'blue';

    const addSubscribe = () => {
        axiosPost.post("/subscribe/addSubscribe", JSON.stringify({
            'subscriberOwnerUserSeq': props?.subscribe?.subscriberOwnerUserSeq,
            'subscribeStatus': 'Y'
        })).then(r => console.log(11))
    }

    return (
        <View style={styles.card}>
            <Image
                source={require('./assets/userImg.png')}
                style={styles.profileImage}
            />
            <View style={styles.infoContainer}>
                <Text style={styles.name}>{props.userInfo.userId}</Text>
                <Text style={styles.role}>{props.userInfo.email}</Text>
                {/* <View style={styles.statsContainer}>
                    <View style={styles.statItem}>
                        <Text style={styles.statValue}>41</Text>
                        <Text style={styles.statLabel}>Articles</Text>
                    </View>
                    <View style={styles.statItem}>
                        <Text style={styles.statValue}>976</Text>
                        <Text style={styles.statLabel}>Followers</Text>
                    </View>
                    <View style={styles.statItem}>
                        <Text style={styles.statValue}>8.5</Text>
                        <Text style={styles.statLabel}>Rating</Text>
                    </View>
                </View>*/}
                <View style={styles.buttonContainer}>
                    <TouchableOpacity style={styles.chatButton}>
                        <Text style={styles.chatButtonText}>CHAT</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={[styles.followButton , {backgroundColor : buttonBackgroundColor}]} onPress={() => addSubscribe()}>
                        <Text style={styles.followButtonText}>FOLLOW</Text>
                    </TouchableOpacity>
                </View>
            </View>
        </View>
    );
}

