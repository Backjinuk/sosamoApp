//
//import UserDto
//import org.modelmapper.ModelMapper
//import kotlin.jvm.java
//
//class ModelMapperUtil {
//
//    companion object {
//        private val modelMapper = ModelMapper()
//
//        /**
//         * userDto to Entity convert
//         * @param userDto
//         * @return User.class
//         */
//
//        fun userDtoToEntity(userDto: UserDto): UserEntity {
//            return modelMapper.map(userDto, UserEntity::class.java)
//        }
//
//        fun userEntityToDto(user : User) : UserDto{
//            return modelMapper.map(user, UserDto::class.java)
//        }
//
//        fun commuDtoToEntity(communityDTO: CommunityDto): Community {
//            return modelMapper.map(communityDTO, Community::class.java)
//        }
//
//        fun commuEntityToDto(community: Community):CommunityDto{
//            return modelMapper.map(community, CommunityDto::class.java)
//        }
//
//        fun subscribeEntityToDto(subscribe: Subscribe):SubscribeDto{
//            return modelMapper.map(subscribe, SubscribeDto::class.java)
//        }
//
//        fun subscribeDtoToEntity(subscribeDto: SubscribeDto):Subscribe{
//            return modelMapper.map(subscribeDto, Subscribe::class.java)
//        }
//
//        fun communityApplyDtoToEntity(communityApplyDto: CommunityApplyDto) : CommunityApply{
//            return modelMapper.map(communityApplyDto, CommunityApply::class.java);
//        }
//
//        fun communityApplyEntityToDto(communityApply: CommunityApply) : CommunityApplyDto{
//            return modelMapper.map(communityApply, CommunityApplyDto::class.java)
//        }
//
//        fun commuEntityToDto(community: Community, status: Char?): CommunityDto {
//            // Community 엔티티를 CommunityDto로 매핑
//            val communityDto = modelMapper.map(community, CommunityDto::class.java)
//
//            // applyStatus를 DTO에 설정
//            communityDto.applyStatus = status
//
//            return communityDto
//        }
//
//    }
//}