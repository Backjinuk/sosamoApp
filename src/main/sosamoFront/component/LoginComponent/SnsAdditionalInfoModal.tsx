import React from "react";
import { Button, Modal, Text, View } from "react-native";
import GeneratorType1 from "../JoinComponent/GeneratorType1";
import createStyles from "./styles";

interface SnsAdditionalInfoModalProps {
    modalVisible: boolean;
    setModalVisible: (visible: boolean) => void;
    nickName: string;
    setNickName: (value: string) => void;
    passwd: string;
    setPasswd: (value: string) => void;
    newPasswd: string;
    setNewPasswd: (value: string) => void;
    BorderBottomColor: string;
    setBoardBottomColor: (color: string) => void;
    loginAxsio: () => void;
}

export default function SnsAdditionalInfoModal(props: SnsAdditionalInfoModalProps) {
    const styles = createStyles();

    return (
        <Modal
            visible={props.modalVisible}
            transparent={true}
            animationType="slide"
            onRequestClose={() => props.setModalVisible(false)}
        >
            <View style={styles.centeredView}>
                <View style={styles.modalView}>
                    <Text style={styles.modalText}>추가 정보 입력</Text>

                    <GeneratorType1
                        name="닉네임"
                        value={props.nickName}
                        setState={props.setNickName}  // 닉네임 입력은 setNickName으로 처리
                        setBoardBottomColor={props.setBoardBottomColor}
                        BorderBottomColor={props.BorderBottomColor}
                    />

                    <GeneratorType1
                        name="비밀번호"
                        value={props.passwd}
                        setState={props.setPasswd}
                        setBoardBottomColor={props.setBoardBottomColor}
                        BorderBottomColor={props.BorderBottomColor}
                    />

                    <GeneratorType1
                        name="비밀번호 재확인"
                        value={props.newPasswd}
                        setState={props.setNewPasswd}
                        setBoardBottomColor={props.setBoardBottomColor}
                        BorderBottomColor={props.BorderBottomColor}
                    />

                    <View
                        style={{
                            height: '10%',
                            width: '100%',
                            flexDirection: "row",
                            justifyContent: 'space-around',
                            alignItems: 'center',
                        }}
                    >
                        <Button
                            title={"닫기"}
                            onPress={() => props.setModalVisible(false)}
                        />
                        <Button
                            title={"회원가입"}
                            onPress={props.loginAxsio}
                        />
                    </View>
                </View>
            </View>
        </Modal>
    );
}
