import React, {useEffect, useRef, useState} from 'react';
import ApplyCommunityList from './Tabs/ApplyCommunityList';
import UserInfo from './Tabs/UserInfo';
import CommunityCalendar from './Tabs/CommunityCalendar';
import MapMain from '../MapCompoent/MapMain.tsx';
import Icon from 'react-native-vector-icons/FontAwesome';
import {Animated, ColorValue, View} from 'react-native';
import {createMaterialBottomTabNavigator} from '@react-navigation/material-bottom-tabs';
// import ChatRoomList from '../ChatComponent/ChatRoomList.tsx';
import {createMaterialTopTabNavigator} from '@react-navigation/material-top-tabs';
import SubscribeInfoCard from './ProfileCard/SubscribeInfoCard.tsx';
import SubscriptionList from './Tabs/SubscriptionList.tsx';
import Config from 'react-native-config';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {jwtDecode} from 'jwt-decode';
import {Badge} from 'react-native-paper';
import {ParamListBase, RouteProp} from "@react-navigation/native";

const Tab = createMaterialBottomTabNavigator();
const TopTab = createMaterialTopTabNavigator();

// TabIcon 컴포넌트
interface TabIconProps {
  name: string;
  size: number;
  color: string;
  focused: boolean;
  badge?: number;
}

const TabIcon = ({ name, size, color, focused, badge }: TabIconProps) => {
  const scale = new Animated.Value(focused ? 1.2 : 1);

  Animated.timing(scale, {
    toValue: focused ? 1.2 : 1,
    duration: 200,
    useNativeDriver: true,
  }).start();

  return badge ? (
    <View>
      <Animated.View style={{ transform: [{ scale }] }}>
        <Icon name={name} size={size} color={color} />
      </Animated.View>
      <Badge children={badge} style={{ position: 'absolute', bottom: 10, left: 15 }} />
    </View>
  ) : (
    <Animated.View style={{ transform: [{ scale }] }}>
      <Icon name={name} size={size} color={color} />
    </Animated.View>
  );
};
// TopTabNavigator를 컴포넌트 외부로 이동
const TopTabNavigator = () => {
  return (
    <TopTab.Navigator
      screenOptions={({route}) => ({
        headerShown: false,
        tabBarActiveTintColor: '#63CC63',
        tabBarInactiveTintColor: 'gray',
      })}>
      <TopTab.Screen
        name="구독"
        component={SubscriptionList} // 구독 목록 화면
      />
      <TopTab.Screen
        name="신청"
        component={ApplyCommunityList} // 신청 목록 화면
      />
    </TopTab.Navigator>
  );
};

export default function TabNavigation() {
  const [messages, setMessages] = useState<number>(0);
  const debug = false;
  const log = (message?: any, ...optionalParams: any[]) => {
    if (debug) console.log(message, ...optionalParams);
  };
  const wsurl = Config.CHAT_URL;
  // @ts-ignore
    const client = useRef<WebSocket>();
  useEffect(() => {
    connect();
    return () => {
      disconnect();
    };
  }, []);

  const connect = async () => {
    if (wsurl) {
      const accessToken = await AsyncStorage.getItem('AccessToken');
      const accessTokenData: any = jwtDecode(accessToken!);
      const refreshToken = await AsyncStorage.getItem('RefreshToken');
      client.current = new WebSocket(
        wsurl + `?type=countMessages&userSeq=${accessTokenData.userSeq}`,
        null,
        {
          headers: {
            AccessToken: `Bearer ${accessToken}`,
            RefreshToken: `${refreshToken}`,
          },
        },
      );
      client.current.onopen = () => {
        log('소켓 통신 활성화');
      };
      client.current.onmessage = e => {
        log('받은 데이터 : ' + e.data);
        const jsondata = JSON.parse(e.data);
        switch (jsondata.type) {
          case 'message':
            setMessages(prev => prev + 1);
            break;
          case 'read':
            setMessages(prev => prev - jsondata.payload);
            break;
          case 'count':
            setMessages(jsondata.payload);
            break;
        }
      };
      client.current.onerror = e => {
        log('에러 발생 : ' + e.message);
      };
      client.current.onclose = e => {
        log('소켓 통신 해제');
      };
    }
  };

  const disconnect = () => {
    client.current?.close();
  };

  return (
    <Tab.Navigator
      screenOptions={({route} : {route : RouteProp<ParamListBase, string>}) => ({
        headerShown: false,
        tabBarIcon: ({focused, color} : {focused : boolean , color : string}) => {
          let iconName = '';

          if (route.name === '지도') {
            iconName = focused ? 'map' : 'map-o';
          } else if (route.name === '구독목록') {
            iconName = focused ? 'heart' : 'heart-o';
          } else if (route.name === '캘린더') {
            iconName = focused ? 'calendar' : 'calendar';
          } else if (route.name === '내정보') {
            iconName = focused ? 'user-circle' : 'user-circle-o';

          } else if (route.name === '채팅') {
            iconName = focused ? 'comment' : 'comment-o';
          }

          return route.name === '채팅'
            return <TabIcon name={iconName} size={20} color={color} focused={focused} badge={route.name === '채팅' ? messages : undefined} />;
        },
        tabBarActiveTintColor: '#63CC63',
        tabBarInactiveTintColor: 'gray',
      })}>
      <Tab.Screen name="지도" component={MapMain} />
      <Tab.Screen name="캘린더" component={CommunityCalendar} />
      <Tab.Screen
        name="구독목록"
        component={TopTabNavigator} // 상단 탭 네비게이션을 연결
      />
{/*
      <Tab.Screen name="채팅" component={ChatRoomList} />
*/}
      <Tab.Screen name="내정보" component={UserInfo} />
    </Tab.Navigator>
  );
}
