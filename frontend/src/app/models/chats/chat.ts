import { ChatMessage } from './chat-message';

export interface Chat {
  id: string,
  senderName: string,
  senderUsername: string,
  senderProfileImage: string,
  lastMessage: string,
  chatMessages: ChatMessage[],
}
