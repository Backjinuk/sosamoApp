//package com.example.myapp.Config
//
//import com.example.myapp.Controller.ChatController
//import com.example.myapp.Dto.ChatRoomDto
//import com.example.myapp.Entity.Chat
//import com.example.myapp.Service.chat.ChatService
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.SerializationFeature
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
//import com.fasterxml.jackson.module.kotlin.readValue
//import org.modelmapper.ModelMapper
//import org.slf4j.LoggerFactory
//import org.springframework.stereotype.Component
//import org.springframework.web.socket.CloseStatus
//import org.springframework.web.socket.TextMessage
//import org.springframework.web.socket.WebSocketSession
//import org.springframework.web.socket.handler.TextWebSocketHandler
//import java.net.URI
//import kotlin.jvm.java
//
//@Component
//class ChatHandler(
//    private val chatController: ChatController,
//    private val chatService: ChatService
//) : TextWebSocketHandler() {
//
//    private val logger = LoggerFactory.getLogger(ChatHandler::class.java)
//    private val sessionsForCount = mutableMapOf<Long, WebSocketSession>()//userSeq, session
//    private val sessionsForPublicRoomList = mutableMapOf<Long, WebSocketSession>()//userSeq, session
//    private val sessionsForPrivateRoomList = mutableMapOf<Long, WebSocketSession>()//userSeq, session
//    private val sessionsForChat = mutableMapOf<Long, WebSocketSession>()//userSeq, session
//    private val rooms = mutableMapOf<String, ArrayList<WebSocketSession>>()//roomId, sessions
//    private val mapper = ObjectMapper()
//    private val modelMapper: ModelMapper = ModelMapper()
//
//    init {
//        mapper.registerModule(JavaTimeModule())
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//        chatController.sessionsForPublicRoomList = sessionsForPublicRoomList
//        chatController.mapper = mapper
//    }
//
//    override fun afterConnectionEstablished(session: WebSocketSession) {
//        logger.info("Connected to session: ${session.id}")
//        logger.info("Connected to session: ${session.uri}")
//        val inputMap = parseUri(session.uri)
//        when(inputMap["type"]){
//            "chatRoom"->{
//                logger.info("Connected to session: chatRoom")
//                val roomId = inputMap["roomId"]!!
//                val returnMap = mutableMapOf<String, Any?>()
//                returnMap["type"] = "message"
//                returnMap["payload"] = chatService.entrance(roomId)
//
//                session.sendMessage(messageFrom(returnMap))
//                val room = rooms.getOrDefault(roomId, arrayListOf())
//                room.add(session)
//                rooms[roomId] = room
//            }
//            "chatRoomList"->{
//                logger.info("Connected to session: chatRoomList")
//                val userSeq = inputMap["userSeq"]!!.toLong()
//                val roomType = inputMap["roomType"]!!
//                val returnMap = mutableMapOf<String, Any?>()
//                returnMap["type"] = "rooms"
//                returnMap["payload"] = chatController.findMyChatRooms(userSeq, roomType)
//                session.sendMessage(messageFrom(returnMap))
//                when(roomType){
//                    "public"->sessionsForPublicRoomList[userSeq] = session
//                    "private"->sessionsForPrivateRoomList[userSeq] = session
//                }
//            }
//            "countMessages"->{
//                logger.info("Connected to session: countMessages")
//                val userSeq = inputMap["userSeq"]!!.toLong()
//                val returnMap = mutableMapOf<String, Any?>()
//                returnMap["type"] = "count"
//                returnMap["payload"] = chatController.countMyMessages(userSeq)
//                session.sendMessage(messageFrom(returnMap))
//                sessionsForCount[userSeq] = session
//            }
//        }
//    }
//
//    override fun handleTextMessage(session: WebSocketSession, data: TextMessage) {
//        logger.info("Received message: ${data.payload}")
//        val inputMap = mapper.readValue<Map<String, Any?>>(content = data.payload)
//        when(inputMap["type"]){
//            "message"->{
//                val map = mapper.readValue<Map<String, String>>(content = "${inputMap["payload"]}")
//                val chatter = map["chatter"]?.toLong()
//                val chatterId = map["chatterId"]
//                val content = map["content"]
//                val roomId = map["roomId"]!!
//                val chatId = map["id"]!!
//                var chatRoom = chatService.findByRoomId(roomId)
//                if(chatRoom==null){
//                    chatRoom = chatController.saveTempRoom(roomId)
//                }
//                var unread = mutableListOf<Long>()
//                for(userSeq in chatRoom.chatters){
//                    if(userSeq!=chatter) unread.add(userSeq)
//                }
//                val chat = Chat.Builder().setId(chatId).setChatter(chatter!!).setChatterId(chatterId!!).setContent(content!!).setUnread(unread).setRoomId(roomId!!).build()
//                chatRoom.chatTime = chat.chatTime
//                chatRoom.content = chat.content
//                chatService.updateChatRoom(chatRoom)
//                chatService.chatting(chat)
//                logger.info("Received message: $chat")
//                val returnMap = mutableMapOf<String, Any>()
//                returnMap["type"] = "message"
//                returnMap["payload"] = listOf(chat)
//                logger.info("Received message: $chat")
//                val room = rooms[roomId]!!
//                for(roomSession in room){
//                    if(roomSession.isOpen) roomSession.sendMessage(messageFrom(returnMap))
//                    else room.remove(roomSession)
//                }
//
//                val chatRoomDto = modelMapper.map(chatRoom, ChatRoomDto::class.java)
//                var sessionsForRoomList = sessionsForPublicRoomList
//                if(chatRoom.type == "private") sessionsForRoomList = sessionsForPrivateRoomList
//                for(userSeq in chatRoom.chatters){
//                    chatRoomDto.unreadMessages = chatService.countUnreadRoomMessage(userSeq, roomId)
//                    returnMap["payload"] = listOf(chatRoomDto)
//                    val message = messageFrom(returnMap)
//                    sessionsForRoomList[userSeq]?.sendMessage(message)
//                    sessionsForCount[userSeq]?.sendMessage(message)
//                }
//            }
//            "read"->{
//                val map = mapper.readValue<Map<String, String>>(content = "${inputMap["payload"]}")
//                val reader = map["reader"]!!.toLong()
//                val roomId = map["roomId"]!!
//                val returnMap = mutableMapOf<String, Any?>()
//                returnMap["type"] = "read"
//                val list = chatController.readMessage(reader, roomId)
//                returnMap["payload"] = list
//
//                val room = rooms[roomId]!!
//                for(roomSession in room){
//                    if(roomSession == session)continue
//                    if(roomSession.isOpen) roomSession.sendMessage(messageFrom(returnMap))
//                    else room.remove(roomSession)
//                }
//
//                returnMap["payload"] = list.size
//                sessionsForCount[reader]?.sendMessage(messageFrom(returnMap))
//
//                returnMap["payload"] = roomId
//                val message = messageFrom(returnMap)
//                sessionsForPublicRoomList[reader]?.sendMessage(message)
//                sessionsForPrivateRoomList[reader]?.sendMessage(message)
//            }
//            "count"->{
//                val userSeq = parseUri(session.uri)["userSeq"]!!.toLong()
//                val returnMap = mutableMapOf<String, Any?>()
//                returnMap["type"] = "count"
//                returnMap["payload"] = chatController.countMyMessages(userSeq)
//                session.sendMessage(messageFrom(returnMap))
//            }
//        }
//    }
//
//    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
//        val inputMap = parseUri(session.uri)
//        when(inputMap["type"]){
//            "chatRoom"->{
//                val roomId = inputMap["roomId"]!!
//                val room = rooms[roomId]!!
//                room.remove(session)
//                println(room.size)
//                if(room.isEmpty()) rooms.remove(roomId)
//                if(chatService.findByRoomId(roomId)==null){
//                    chatController.deleteTempRoom(roomId)
//                }
//            }
//            "chatRoomList"->{
//                val userSeq = inputMap["userSeq"]!!.toLong()
//                sessionsForPublicRoomList.remove(userSeq)
//                sessionsForPrivateRoomList.remove(userSeq)
//            }
//            "countMessages"->{
//                val userSeq = inputMap["userSeq"]!!.toLong()
//                sessionsForCount.remove(userSeq)
//            }
//        }
//
//        logger.info("Session closed: ${session.id}")
//    }
//
//    fun messageFrom(map:Map<String, Any?>):TextMessage{
//        return TextMessage(mapper.writeValueAsString(map))
//    }
//
//    fun parseUri(uri:URI?):Map<String, String>{
//        val inputMap = mutableMapOf<String, String>()
//        for(entries in uri.toString().split("?")[1].split("&")){
//            val key = entries.split("=")[0]
//            val value = entries.split("=")[1]
//            inputMap[key] = value
//        }
//        return inputMap
//    }
//
//}
