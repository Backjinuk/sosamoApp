import {StyleSheet, Text, TextInput, View} from "react-native";
import React from "react";
import styles from "./styles.ts";
import createStyles from "./styles.ts";
import axios from "axios";
import Config from "react-native-config";

export default function GeneratorType1(props: any) {

    const styles = createStyles(props);
    const api = Config.API_BASE_URL

    const getFindUserId= (value: any) => {
        axios.post(api+"/user/getFindUserId", JSON.stringify({
            userId : value
        }), {
            headers : {"Content-type" : "application/json"}
        }).then(res => {

        }).catch(e => {
            console.log(e)
        })
    }

    return (
        <View style={styles.TossTextInputView}>
            <Text style={{color: 'lightgray'}}>{props.name}</Text>
            <TextInput
                value={props.value}
                style={[styles.TossTextInput, {borderBottomColor: props.BorderBottomColor}]}
                onChangeText={(value) => {
                    props.setState(value);
                    if(props.name === '아이디') {
                        getFindUserId(value);
                    }
                }}
                placeholder={props.name}
                onFocus={() => {
                    props.setBoardBottomColor('#0064FF')
                }}
                onEndEditing={() => {
                    props.setBoardBottomColor('lightgray')
                }}
            />
        </View>
    )
};