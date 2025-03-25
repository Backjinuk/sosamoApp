import React, {useEffect, useState} from 'react';
import {NaverMapMarkerOverlay} from '@mj-studio/react-native-naver-map';

export default function CommunityMaker(props: any) {

    const [marker, setMarker] = useState<Community | null>(null);

    useEffect(() => {
        setMarker(props.marker);
    }, [props.marker]);

    if (!marker) {
        return null; // marker가 없으면 아무 것도 렌더링하지 않음
    }

    const setPosition = (marker: Community) => {
        props.setPosition(marker);
    };

    const handleMarkerTap = () => {
        props.setPosition(marker);
        props.setMaker(marker);
        props.setOpenModal();
        props.handlePresentModalPress();
    };

    return (
            <NaverMapMarkerOverlay
                latitude={marker.latitude}
                longitude={marker.longitude}
                onTap={handleMarkerTap}
                width={30}  // 마커의 너비
                height={40}  // 마커의 높이
                anchor={{ x: 0.5, y: 1 }} // 앵커 포인트 설정


                caption={marker.applyStatus === 'Y' ? {
                    text:`${marker.commuTitle}`,
                    align:"Top",
                    offset:5,
                    textSize:13
                }: undefined}

                subCaption={marker.applyStatus === 'Y' ? {
                    text:`${marker.commuMeetingTime}`,
                    textSize:10,
                    color:'red'
                }: undefined}


            />
    );
}