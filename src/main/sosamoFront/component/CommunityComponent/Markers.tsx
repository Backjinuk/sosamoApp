import {NaverMapMarkerOverlay} from "@mj-studio/react-native-naver-map";
import LocationMarker from "../MapCompoent/LocationMarker.tsx";
import {View} from "react-native";
import CommunityMaker from "./CommunityMaker.tsx";
import React from "react";

export default function Markers(props : any){
    return(
        <>
            {props.position && (
                <NaverMapMarkerOverlay
                    latitude={props.position.latitude}
                    longitude={props.position.longitude}
                    onTap={() => {
                        props.setPosition(props.position);
                    }}
                    anchor={{x: 0.5, y: 1}}
                />
            )}

            {props.locations.length !== 0 &&
                props.locations.map((location: { latitude: any; longitude: any; title: any; }) => (
                    <LocationMarker
                        key={location.latitude + location.longitude + location.title}
                        location={location}
                        setPosition={props.setPosition}
                    />
                ))}

            {props.markers.length > 0 &&
                props.markers.map((marker: { latitude: any; longitude: any; commuTitle: any; }, index: React.Key | null | undefined) => (
                    <View key={index}>
                        {/* CommunityMaker 컴포넌트 */}
                        <CommunityMaker
                            key={marker?.latitude + marker?.longitude + marker?.commuTitle}
                            marker={marker}
                            handlePresentModalPress={props.handlePresentModalPress}
                            setOpenModal={props.setOpenModal}
                            setPosition={props.setPosition}
                            setMaker={props.setMaker}
                        />
                    </View>
                ))
            }
        </>
    )
}