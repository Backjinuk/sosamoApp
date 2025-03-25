import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 16,
    },
    header: {
        marginBottom: 16,
    },
    headerTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    formGroup: {
        marginBottom: 16,
    },
    inputContainer: {
        borderBottomWidth: 1,
        borderBottomColor: '#ccc',
        paddingVertical: 8,
    },
    textAreaContainer: {
        borderBottomWidth: 1,
        borderBottomColor: '#ccc',
        paddingVertical: 8,
        height: 100,
    },
    map: {
        height: 200,
        marginTop: 8,
        marginBottom: 8,
    },
    textArea: {
        height: 100,
        textAlignVertical: 'top',
    },
    input: {
        borderWidth: 1,
        borderColor: '#ccc',
        borderRadius: 4,
        padding: 8,
        fontSize: 16,
    },
    searchBox: {
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: '#fff',
        borderRadius: 25,
        paddingVertical: 5,
        paddingHorizontal: 10,
        height: 50,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 6,
        elevation : 10
    },
    input2: {
        flex: 1,
        fontSize: 16,
        paddingHorizontal: 10,
        color: '#333',
    },
    iconButton: {
        padding: 5,
    },

    InfoViewContainer: {
        width: '100%',
        height : "100%",
        borderTopRightRadius: 10,
        borderTopLeftRadius: 10,
        backgroundColor: "#fff",
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 6,
        elevation: 20,
        opacity: 1, // Opacity 값은 0부터 1까지 설정해야 함
    },

    infoCard: {
        display : "flex",
        position: 'absolute',
        marginTop : -10, // 상단에 위치하도록 설정
        width: '100%',
        height : 100,
        padding: 10,
        flexDirection: 'row',
    },

    cardHeader: {
        flex: 1,
    },
    title: {
        fontSize: 16,
        fontWeight: 'bold',
        marginBottom: 5,
    },
    subInfo: {
        fontSize: 14,
        color: '#555',
    },
    image: {
        flex: 1,
        borderRadius: 10,
        marginLeft: 10,
    },

    buttonContainer: {
        flexDirection : 'row',
        width: '100%',
        padding: 10,
        top : "20%",
        display : "flex",
    },

    button: {
        backgroundColor: '#007BFF', // 버튼 배경 색상
        padding: 5,
        borderRadius: 5,
        marginLeft : 5,

    },

    buttonText: {
        color: '#FFFFFF', // 글자 색상
        fontSize: 13, // 글자 크기
        fontWeight : "bold",
        textAlign: 'center',
    },
    searchContainer: {
        position: 'absolute',
        top: 20,
        left: 10,
        right: 10,
        zIndex: 1, // 지도보다 약간 낮은 값
    },


    //UserProfileModal

    UserProfileModalContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    openButton: {
        fontSize: 18,
        color: '#007BFF',
    },
    modal: {
        justifyContent: 'flex-end',
        margin: 0,
    },
    modalContent: {
        backgroundColor: 'white',
        padding: 20,
        borderTopLeftRadius: 20,
        borderTopRightRadius: 20,
        alignItems: 'center',
    },
    profileImage: {
        width: 100,
        height: 100,
        borderRadius: 50,
        marginBottom: 10,
    },
    userName: {
        fontSize: 22,
        fontWeight: 'bold',
        marginBottom: 5,
    },
    userRole: {
        fontSize: 16,
        color: 'grey',
        marginBottom: 10,
    },
    userAddress: {
        fontSize: 14,
        color: 'grey',
        textAlign: 'center',
        marginBottom: 20,
    },
    UserProfileModalButtonContainer: {
        flexDirection: 'row',
        marginBottom: 20,
    },
    followButton: {
        backgroundColor: '#007BFF',
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 5,
        marginRight: 10,
    },
    messageButton: {
        backgroundColor: '#28A745',
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 5,
    },
    UserProfileModalButtonText: {
        color: 'white',
        fontSize: 16,
    },
    socialIcons: {
        flexDirection: 'row',
        justifyContent: 'center',
    },
    icon: {
        marginHorizontal: 10,
    },


});

export default styles;
