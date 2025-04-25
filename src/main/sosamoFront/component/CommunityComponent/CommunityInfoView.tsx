import React, { useEffect, useState } from "react";
import { Text, TouchableOpacity, View, Image } from "react-native";
import styles from "./styles.ts";
import axiosPost from "../../Util/AxiosUtil.ts";
import UserProfileModal from "./UserProfileModal.tsx";
import CommunityAddForm from "./CommunityAddForm.tsx";

export default function CommunityInfoView(props: any) {
    const [marker, setMarker] = useState<Community | null>(null);
    const [openModal, setOpenModal] = useState(false);
    const [subscribe, setSubscribe] = useState<Subscribe | null>(null);
    const [communityApply, setCommunityApply] = useState<CommunityApply | null>(null);
    const [rander, setRander] = useState<Boolean>(false)

    // API 호출하여 communityApply 상태를 설정
    const getCommuApplyUser = () => {

        axiosPost.post("/communityApply/getCommuApplyUser", JSON.stringify(marker))
            .then((res) => {
                setCommunityApply(res.data.applyStatus);
            })
            .catch((err) => {
                console.error("Error fetching community apply data:", err);
            });

    };

    // API 호출하여 subscribe 상태를 설정
    const getSubscribe = () => {
        axiosPost.post("/subscribe/getSubscribe", JSON.stringify({
            'subscriberOwnerUserSeq': props.marker.commuWrite.userSeq
        }))

	 
        .then(res => {
            setSubscribe(res.data);
        })
        .catch(err => {
            console.error("Error fetching subscribe data:", err);
        });
    };

    const getCommunityInfo = () => {
        axiosPost.post("/commu/getCommunityInfo", JSON.stringify({
            "commuSeq" : props.marker.commuSeq
        })).then(  res => {
            console.log(res.data)
        })
    }

    useEffect(() => {
        setMarker(props.marker);
        getCommunityInfo();
        getCommuApplyUser();
    }, [props.marker]);

    if (!marker ||  communityApply == null) {
        return <Text>Loading...</Text>; // 로딩 중 표시
    }

    const commuApplyUser = () => {
        axiosPost.post("/communityApply/commuApplyUser", JSON.stringify(marker))
            .then((res) => {
                console.log(res.data.applyChack)
                props.setCommuPosition();

            })
            .catch((err) => {
                console.error("Error applying for community:", err);
            });
    };

    return (
        <View>
            {/* 지도 오버레이 카드 */}
            <View style={[styles.infoCard]}>
                <View style={styles.cardHeader}>
                    <Text style={styles.title}>{marker.commuTitle || "가게 이름"}</Text>
                    <Text style={styles.subInfo}>{marker.commuComent}</Text>
                </View>
                <Image
                    source={{ uri: 'https://via.placeholder.com/150' }}
                    style={styles.image}
                />
            </View>
            {/* 하단 버튼 */}
            <View style={styles.buttonContainer}>
                <View style={{ flex: 1, display: 'flex', flexDirection: "row", justifyContent: "flex-start" }}>
                    <Text style={[styles.subInfo, { marginTop: 5 }]}>
                        모집인원 : {marker.userCount || 0} / {marker.totalUserCount}
                    </Text>
                </View>
                <View style={{ flex: 1, display: 'flex', flexDirection: "row", justifyContent: "flex-end" }}>

                    {communityApply?.applyStatus === 'Y' ? (
                        <TouchableOpacity style={[styles.button, { backgroundColor: 'red' }]} onPress={commuApplyUser}>
                            <Text style={styles.buttonText}>신청중</Text>
                        </TouchableOpacity>
                    ) : (
                        <TouchableOpacity style={styles.button} onPress={commuApplyUser}>
                            <Text style={styles.buttonText}>신청</Text>
                        </TouchableOpacity>
                    )}
                    <TouchableOpacity style={styles.button} onPress={() => {
                        getSubscribe();
                        setOpenModal(true);
                    }}>
                        <Text style={styles.buttonText}>신청자</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.button} onPress={() => {
                        props.viewMode(marker);
                    }}>
                        <Text style={styles.buttonText}>상세</Text>
                    </TouchableOpacity>
                </View>
            </View>
            <UserProfileModal
                openModal={openModal}
                setOpenModal={setOpenModal}
                commuWrite={props.marker.commuWrite}
                subscribe={subscribe}
            />
        </View>
    );
}
