import {StyleSheet} from "react-native";

 const createStyles = ()=>  {
    return StyleSheet.create({
        container: {
            flex: 1,
        },
        card: {
            flexDirection: 'row',
            backgroundColor: '#333',
            padding: 15,
            borderRadius: 10,
            alignItems: 'center',
            marginVertical: 10,
            marginHorizontal: 20,
            elevation: 5,
        },
        profileImage: {
            width: 90,
            height: 90,
            borderRadius: 10,
        },
        infoContainer: {
            marginLeft: 15,
            flex: 1,
        },
        name: {
            fontSize: 20,
            fontWeight: 'bold',
            color: '#FFF',
        },
        role: {
            fontSize: 16,
            color: '#AAA',
            marginBottom: 10,
        },
        statsContainer: {
            flexDirection: 'row',
            justifyContent: 'space-between',
            marginBottom: 15,
            backgroundColor: '#444',
            padding: 10,
            borderRadius: 5,
        },
        statItem: {
            alignItems: 'center',
        },
        statValue: {
            fontSize: 18,
            fontWeight: 'bold',
            color: '#FFF',
        },
        statLabel: {
            fontSize: 14,
            color: '#BBB',
        },
        buttonContainer: {
            flexDirection: 'row',
            justifyContent: 'space-between',
        },
        chatButton: {
            backgroundColor: '#444',
            paddingVertical: 10,
            paddingHorizontal: 20,
            borderRadius: 5,
            marginRight: 10,
        },
        chatButtonText: {
            color: '#FFF',
            fontWeight: 'bold',
        },
        followButton: {
            backgroundColor: '#2979FF',
            paddingVertical: 10,
            paddingHorizontal: 20,
            borderRadius: 5,
        },
        followButtonText: {
            color: '#FFF',
            fontWeight: 'bold',
        }
    })
}

export default createStyles();