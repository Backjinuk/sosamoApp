import {Button, SafeAreaView, Text, View} from "react-native";
import JoinForm from "./JoinForm.tsx";
import React, {useState} from "react";
import Config from "react-native-config";

export default function JoinFormMain() {
    const [joinEvent, setJoinEvent] = useState(false)
    const [nextStage, setNextStage] = useState(false);
    const api = Config.API_BASE_URL;

    const handleJoinFormSubmit = () => {
        console.log('JoinForm submitted!');
    }

    return (
        <SafeAreaView style={{
            width: '100%',
            height: '100%',
            backgroundColor: 'white'
        }}>
            <View style={{
                width: '90%',
                height: '100%',
                marginLeft: '5%'
            }}>
                <View style={{
                    height: '15%',
                    width: '100%',
                    display: 'flex',
                    justifyContent: 'flex-end',

                }}>
                    <Text style={{
                        fontSize: 24,
                        fontWeight: 'bold',
                    }}>
                        입력한 정보를 확인해주세요
                    </Text>
                </View>

                <JoinForm
                    joinEvent={joinEvent}
                    nextState={nextStage}
                    setNextStage={setNextStage}
                />

                <View style={{
                    height: '35%',
                    width: '100%',
                    display: 'flex',
                    justifyContent: 'center'
                }}>
                    {!nextStage ?
                        <Button title={'다음 단계'} onPress={() => setNextStage(true)}/> :
                        <Button title={'회원 가입'} onPress={() => setJoinEvent(true)}/>
                    }
                </View>

            </View>
        </SafeAreaView>
    )
}