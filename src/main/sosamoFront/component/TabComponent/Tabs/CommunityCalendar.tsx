import {Text, View} from "react-native";
import React from "react";
import UserProfileModal from "../../CommunityComponent/UserProfileModal.tsx";

export default function CommunityCalendar(){
    return(
        <View style={{flex : 1}}>
            <UserProfileModal/>
        </View>
    )
}