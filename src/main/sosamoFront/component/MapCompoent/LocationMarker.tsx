import React from 'react';
import {NaverMapMarkerOverlay} from '@mj-studio/react-native-naver-map';
import {Alert} from "react-native";

export default function LocationMarker(props: any) {
  const location: Community = props.location;

  const setPosition = (location: Community) => {
      props.setPosition(location);
  };

  return (
    <NaverMapMarkerOverlay
      key={location.latitude + location.longitude + location.commuTitle}
      latitude={location.latitude}
      longitude={location.longitude}
      onTap={() => {
        setPosition(location);
        Alert.alert(location.commuTitle)
      }}
      anchor={{x: 0.5, y: 1}}
    />
  );
}
