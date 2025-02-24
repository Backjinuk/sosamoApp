import {Modal, Text, TextInput, View} from 'react-native';
import React, {useState} from 'react';
import createStyles from './styles.ts';
import DropDownPicker from "react-native-dropdown-picker";

const GeneratorType2 = (props: any, ref: any) => {
    const styles = createStyles(props);
    const [open, setOpen] = useState(false);
    const [modalVisible, setModalVisible] = useState(false);
    const [items, setItems] = useState([
        {label: '선택', value: ''},
        {label: '직접 입력', value: 'custom'},
        {label: 'example.com', value: 'example.com'},
        {label: 'naver.com', value: 'naver.com'},
        {label: 'gmail.com', value: 'gmail.com'},
        {label: 'yahoo.com', value: 'yahoo.com'},
        {label: 'daum.net', value: 'daum.net'},
    ]);

    const handleEmailChange = (text: string) => {
        setItems([...items, {label: text, value: text}]);
        props.setValue2(text);
        setModalVisible(false);
    };

    return (
        <View style={styles.TossTextInputView}>
            <Text style={{color: 'lightgray'}}>{props.name}</Text>
            <View style={styles.row}>
                <TextInput
                    value={props.value1}
                    style={[styles.TossTextInput_half]}
                    onChangeText={props.setValue1}
                    maxLength={(props.type === 'R ') ? 6 : 30}
                    placeholder={props.name}
                    onFocus={() => {
                        props.setBoardBottomColor1('#0064FF');
                    }}
                    onEndEditing={() => {
                        props.setBoardBottomColor1('lightgray');
                    }}
                />
                <View style={styles.separator}>
                    <Text style={styles.separatorText}>
                        {props.type === 'R' ? '-' : '@'}
                    </Text>
                </View>
                {props.type === 'R' ? (
                    <>
                        <TextInput
                            value={props.value2 || ''}
                            style={[styles.TossTextInput_half, {
                                width: '8%',
                                borderBottomColor: props.BorderBottomColor2
                            }]}
                            onChangeText={props.setValue2}
                            maxLength={1}
                            onFocus={() => {
                                props.setBoardBottomColor2('red');
                            }}
                            onEndEditing={() => {
                                props.setBoardBottomColor2('lightgray');
                            }}
                        />
                        <TextInput
                            style={[styles.TossTextInput_half, styles.placeholder]}
                            value={'000000'}
                            secureTextEntry={true}
                            editable={false}
                        />
                    </>
                ) : (
                    <>
                        <DropDownPicker
                            items={items}
                            containerStyle={{height: 50, width: '45%'}}
                            style={{backgroundColor: 'white'}}
                            open={open}
                            setOpen={setOpen}
                            setValue={props.setValue2}
                            value={props.value2 || ''}
                            onChangeValue={(value) => {
                                if (value === 'custom') {
                                    setModalVisible(true);
                                } else {
                                    props.setValue2(value);
                                }
                            }}
                        />
                        <Modal
                            animationType="slide"
                            transparent={true}
                            visible={modalVisible}
                        >
                            <View style={styles.centeredView}>
                                <View style={styles.modalView}>
                                    <Text style={styles.modalText}>직접 입력</Text>
                                    <TextInput
                                        onChangeText={handleEmailChange}
                                        placeholder="이메일 주소 입력"
                                        style={styles.modalInput}
                                    />
                                </View>
                            </View>
                        </Modal>
                    </>
                )}
            </View>
        </View>
    );
};

export default React.forwardRef(GeneratorType2);