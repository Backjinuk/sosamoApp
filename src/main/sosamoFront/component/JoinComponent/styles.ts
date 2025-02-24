import { StyleSheet } from 'react-native';

interface StylesProps {
    BorderBottomColor: string;
}

const createStyles = (props: StylesProps) => {
    return StyleSheet.create({
        TossTextInputView: {
            width: '100%',
            height: '25%',
            marginTop: '5%',
            display: 'flex',
            justifyContent: 'center',
        },

        TossTextInput: {
            width: '100%',
            height: '60%',
            backgroundColor: 'white',
            borderBottomWidth: 1,
            fontWeight: '600',
        },

        TossTextInput_half: {
            width: '45%',
            height: '100%',
            backgroundColor: 'white',
            borderBottomColor: props.BorderBottomColor,
            borderBottomWidth: 1,
            fontWeight: '600',
        },

        row: {
            flexDirection: 'row',
            height: '60%',
            justifyContent: 'center',
            alignItems: 'center'
        },
        separator: {
            width: '10%',
            flexDirection: 'row',
            justifyContent: 'center',
        },
        separatorText: {
            fontSize: 24,
            fontWeight: '300'
        },
        placeholder: {
            borderBottomWidth: 0,
            width: '37%',
            fontSize: 34,
            letterSpacing: 5
        },

        // 모달 스타일
        centeredView: {
            flex: 1,
            justifyContent: "center",
            alignItems: "center",
            marginTop: 22
        },
        modalView: {
            margin: 20,
            backgroundColor: "white",
            borderRadius: 20,
            padding: 35,
            alignItems: "center",
            shadowColor: "#000",
            shadowOffset: {
                width: 0,
                height: 2
            },
            shadowOpacity: 0.25,
            shadowRadius: 4,
            elevation: 5
        },
        modalInput: {
            marginBottom: 15,
            textAlign: "center"
        },
        modalText: {
            marginBottom: 15,
            textAlign: "center"
        },

    });
};

export default createStyles;
