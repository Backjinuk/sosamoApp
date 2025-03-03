import React from 'react'
import {Button, Image, StyleSheet, Text, TextInput, TouchableOpacity, View} from "react-native";
import Icon from "react-native-vector-icons/FontAwesome";
import styles from "../CommunityComponent/styles.ts";

export default function MapSearchBar(props: any) {
    return (

        // 배경 컴포넌트에 opacity 적용

        <View style={styles.searchBox}>

            <TextInput
                style={styles.input2}
                placeholder="장소, 버스, 지하철, 주소 검색"
                placeholderTextColor="#999"
                value={props.keyword}
                onChangeText={props.setKeyword}
            />

            {/* 검색 버튼 */}
            <TouchableOpacity
                onPress={() => {
                    props.getLocations();
                    props.privateSetPosition(undefined);
                }}
                style={styles.iconButton}
            >
                <Icon name="search" size={20} color="#2196F3" />
            </TouchableOpacity>

            {/* 메뉴 아이콘 (선택 사항) */}
            <TouchableOpacity style={styles.iconButton}>
                <Icon name="bars" size={20} color="#666" />
            </TouchableOpacity>

            {/* 마이크 아이콘 (선택 사항) */}
            <TouchableOpacity style={styles.iconButton}>
                <Icon name="microphone" size={20} color="#666" />
            </TouchableOpacity>
        </View>
    );
};

