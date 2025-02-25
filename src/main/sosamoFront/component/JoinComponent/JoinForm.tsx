import GeneratorType1 from "./GeneratorType1.tsx";
import {Alert, Button, View} from "react-native";
import React, {useEffect, useState} from "react";
import GeneratorType2 from "./GeneratorType2.tsx";
import axios from "axios";
import Config from "react-native-config";
import {useNavigation} from "@react-navigation/native";
import {NativeStackNavigationProp} from "@react-navigation/native-stack";
import {RootStackParamList} from "../../Types/RootStackParamList.ts";

interface JoinFormProps {
    joinEvent: boolean,
    nextState: boolean,
    setNextStage?: (value: (((prevState: boolean) => boolean) | boolean)) => void
}

const JoinForm: React.FC<JoinFormProps> = ({joinEvent, nextState, setNextStage}) => {
    const [BorderBottomColor1, setBoardBottomColor1] = useState('lightgray')
    const [BorderBottomColor2, setBoardBottomColor2] = useState('lightgray')
    const [BorderBottomColor3, setBoardBottomColor3] = useState('lightgray')
    const [BorderBottomColor4, setBoardBottomColor4] = useState('lightgray')
    const [BorderBottomColor5, setBoardBottomColor5] = useState('lightgray')
    const [BorderBottomColor6, setBoardBottomColor6] = useState('lightgray')

    const [phoneNum, setPhoneNum] = useState('');
    const [name, setName] = useState('')
    const [userId, setUserId] = useState('')
    const [passwd, setPasswd] = useState('')
    const [RNum1, setRUum1] = useState('');
    const [RNum2, setRUum2] = useState('');
    const [email1, setEmail1] = useState('')
    const [email2, setEmail2] = useState('')

    const navigation = useNavigation<NativeStackNavigationProp<RootStackParamList>>();


    const api = Config.API_BASE_URL;


    useEffect(() => {
        if(nextState){
            const fields = { phoneNum, name, RNum1, RNum2, email1, email2 };

            if (setNextStage) {
                setNextStage( FormChecked(fields))
            }
        }
        if (joinEvent) {
            const fields = { userId, passwd };
            if(FormChecked(fields)){
                axios.post(`/user/userJoin`, JSON.stringify({
                    userId,
                    passwd,
                    name,
                    phoneNum,
                    rNum : RNum1 + RNum2,
                    email : email1 + '@' + email2
                }), {
                    headers: {'Content-Type': 'application/json'}
                }).then((res) => {
                    if(res){
                        Alert.alert('회원가입에 성공하였습니다.');
                        navigation.navigate('LoginMain');
                    }else{
                        Alert.alert('이미 회원가입된 계정입니다.');
                    }
                })
            }
        }
    }, [nextState, joinEvent]);

    const FormChecked = (fields: { [s: string]: unknown; } | ArrayLike<unknown>): boolean => {
        for (const [key, value] of Object.entries(fields)) {
            if (!value) {
                Alert.alert('입력한 정보를 확인해 주세요');
                return false;
            }
        }
        return true;
    }

    return (
        <>
            <View style={{
                height: '50%',
                width: '100%',
            }}>

                {!nextState ?
                    <>
                        <GeneratorType1
                            name='이름'
                            value={name}
                            setState={setName}
                            setBoardBottomColor={setBoardBottomColor1}
                            BorderBottomColor={BorderBottomColor1}
                        />


                        <GeneratorType1
                            name='휴대폰 번호'
                            value={phoneNum}
                            setState={setPhoneNum}
                            setBoardBottomColor={setBoardBottomColor2}
                            BorderBottomColor={BorderBottomColor2}
                        />

                        <GeneratorType2
                            name={'주민등록번호'}
                            value1={RNum1}
                            value2={RNum2}
                            setValue1={setRUum1}
                            setValue2={setRUum2}
                            type={'R'}
                            setBoardBottomColor1={setBoardBottomColor3}
                            setBoardBottomColor2={setBoardBottomColor4}
                            BorderBottomColor={BorderBottomColor3}
                            BorderBottomColor2={BorderBottomColor4}
                        />

                        <GeneratorType2
                            name={'이메일'}
                            value1={email1}
                            value2={email2}
                            setValue1={setEmail1}
                            setValue2={setEmail2}
                            type={'E'}
                            setBoardBottomColor1={setBoardBottomColor5}
                            setBoardBottomColor2={setBoardBottomColor6}
                            BorderBottomColor={BorderBottomColor5}
                            BorderBottomColor2={BorderBottomColor6}
                        />
                    </> :
                    <>
                        <GeneratorType1
                            name='아이디'
                            value={userId}
                            setState={setUserId}
                            setBoardBottomColor={setBoardBottomColor1}
                            BorderBottomColor={BorderBottomColor1}
                        />

                        <GeneratorType1
                            name='비밀번호'
                            value={passwd}
                            setState={setPasswd}
                            setBoardBottomColor={setBoardBottomColor2}
                            BorderBottomColor={BorderBottomColor2}/>

                    </>
                }

            </View>


        </>

    )
}

export default JoinForm;
