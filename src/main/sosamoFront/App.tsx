/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */
import React from 'react';
import LoginMain from "./component/LoginComponent/LoginMain.tsx";
import {createNativeStackNavigator} from "@react-navigation/native-stack";
import {RootStackParamList} from "./Types/RootStackParamList.ts";
import {SafeAreaView, Text} from 'react-native';
import {NavigationContainer} from '@react-navigation/native';
import JoinFormMain from "./component/JoinComponent/JoinFormMain.tsx";

function App(): React.JSX.Element {
    const Stack = createNativeStackNavigator<RootStackParamList>();

    return (
        <SafeAreaView style={{flex: 1}}>
            <NavigationContainer>
                <Stack.Navigator initialRouteName="LoginMain" screenOptions={{headerShown: false}}>
                    <Stack.Screen name="Join" component={JoinFormMain} options={{headerShown: false}}/>
                    <Stack.Screen name="LoginMain" component={LoginMain} options={{headerShown: false}}/>
                    {/*
                        <Stack.Screen name="LoginForm" component={LoginForm} options={{headerShown: false}}/>
                        <Stack.Screen name="ChatScreen" component={ChatScreen} options={{headerShown: false}}/>
                        <Stack.Screen name="TabNavigation" component={TabNavigation} options={{headerShown: false}}/>
*/}
                </Stack.Navigator>
            </NavigationContainer>
        </SafeAreaView>
    );
}
export default App;
