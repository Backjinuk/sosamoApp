import React, { useEffect, useState } from "react";
import axiosPost from "../../../Util/AxiosUtil.ts";
import SubscribeInfoCard from "../ProfileCard/SubscribeInfoCard.tsx";
import styles from "../ProfileCard/styles.ts";
import { View } from "react-native";

export default function SubscriptionList() {
    const [subscribeList, setSubscribeList] = useState<Subscribe[]>([]);

    useEffect(() => {
        axiosPost.post('/subscribe/getSubscribeList')
            .then((res) => {
                setSubscribeList(res.data.subscriberDtoList);
            })
            .catch((error) => {
                console.error(error);
            });
    }, []);

    return (
        <View style={styles.container}>
            {subscribeList.map((subscribe, index) => {
                return (
                    <SubscribeInfoCard
                        key={index} // 리액트에서 리스트를 렌더링할 때는 key prop이 필요합니다.
                        userInfo={subscribe.subscribeUser} // subscribeUser 객체를 userInfo로 전달
                        subscribe={subscribe}
                    />
                );
            })}
        </View>
    );
}
