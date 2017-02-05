package dispatch;

import api.account.Login;
import api.account.Register;
import api.friend.AllFriend;
import api.friend.FriendAdd;
import api.friend.FriendRemove;
import api.future.*;
import api.group.GroupCreate;
import api.group.GroupDisband;
import api.group.MemberAdd;
import api.group.MemberRemove;
import api.info.FriendInfo;
import api.info.InfoUpdate;
import api.info.SelfInfo;
import api.message.PersonMessage;
import com.oracle.deploy.update.UpdateInfo;
import common.Client;
import io.netty.util.ReferenceCountUtil;
import pojo.*;
import serialize.Serializer;
import transport.HeartbeatHandler;
import transport.HeartbeatManager;
import transport.MessageHolder;
import transport.ProtocolHeader;

import static java.awt.SystemColor.info;

/**
 * 内容分发.
 *
 * @author Yohann.
 */
public class Dispatcher extends Client {

    public static void dispatch(MessageHolder messageHolder) {
        byte sign = messageHolder.getSign();
        if (sign == ProtocolHeader.REQUEST) {
            return;
        }

        switch (sign) {
            case ProtocolHeader.RESPONSE:
                switch (messageHolder.getType()) {
                    case ProtocolHeader.LOGIN:
                        Account aLogin = Serializer.deserialize(messageHolder.getBody(), Account.class);
                        login(messageHolder, aLogin);
                        break;

                    case ProtocolHeader.REGISTER:
                        Account aRegister = Serializer.deserialize(messageHolder.getBody(), Account.class);
                        register(messageHolder, aRegister);
                        break;

                    case ProtocolHeader.PERSON_MESSAGE:
                        personMessage(messageHolder);
                        break;

                    case ProtocolHeader.ADD_FRIEND:
                        friendAdd(messageHolder);
                        break;

                    case ProtocolHeader.REMOVE_FRIEND:
                        friendRemove(messageHolder);
                        break;

                    case ProtocolHeader.ALL_FRIEND:
                        MyFriend myFriend = Serializer.deserialize(messageHolder.getBody(), MyFriend.class);
                        allFriend(messageHolder, myFriend);
                        break;

                    case ProtocolHeader.UPDATE_SELF_INFO:
                        updateInfo(messageHolder);
                        break;

                    case ProtocolHeader.LOOK_SELF_INFO:
                        Info sInfo = Serializer.deserialize(messageHolder.getBody(), Info.class);
                        lookSelfInfo(messageHolder, sInfo);
                        break;

                    case ProtocolHeader.LOOK_FRIEND_INFO:
                        Info fInfo = Serializer.deserialize(messageHolder.getBody(), Info.class);
                        lookFriendInfo(messageHolder, fInfo);
                        break;

                    case ProtocolHeader.CREATE_GROUP:
                        createGroup(messageHolder);
                        break;

                    case ProtocolHeader.DISBAND_GROUP:
                        disbandGroup(messageHolder);
                        break;

                    case ProtocolHeader.ADD_MEMBER:
                        addMember(messageHolder);
                        break;

                    case ProtocolHeader.REMOVE_MEMBER:
                        removeMember(messageHolder);
                        break;

                    default:
                        break;
                }
                break;

            case ProtocolHeader.NOTICE:
                switch (messageHolder.getType()) {
                    case ProtocolHeader.PERSON_MESSAGE:
                        Message pMessage = Serializer.deserialize(messageHolder.getBody(), Message.class);
                        personMessageNotice(pMessage);
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;
        }

        // 释放buffer
        ReferenceCountUtil.release(messageHolder);
    }

    /**
     * 登录响应
     *
     * @param messageHolder
     * @param account
     */
    private static void login(MessageHolder messageHolder, Account account) {
        LoginFutureListener listener = Login.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            // 保存用户信息
            mUsername = account.getUsername();
            mToken = account.getToken();
            // 启动心跳
            mChannel.pipeline().addAfter("IdleStateHandler",
                    "HeartbeatHandler", new HeartbeatHandler(new HeartbeatManager()));
            listener.onSuccess(mUsername);
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }

    /**
     * 注册响应
     *
     * @param messageHolder
     * @param account
     */
    private static void register(MessageHolder messageHolder, Account account) {
        RegisterFutureListener listener = Register.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            listener.onSuccess(account.getUsername());
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }

    /**
     * 个人消息响应
     *
     * @param messageHolder
     */
    private static void personMessage(MessageHolder messageHolder) {
        PersonMessageFutureListener listener = PersonMessage.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            listener.onSuccess();
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }

    /**
     * 个人消息通知
     *
     * @param message
     */
    private static void personMessageNotice(Message message) {
        mReceiver.personMessage(message.getSender(), message.getContent(), message.getTime());
    }

    /**
     * 添加好友响应
     *
     * @param messageHolder
     */
    private static void friendAdd(MessageHolder messageHolder) {
        FriendAddFutureListener listener = FriendAdd.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            listener.onSuccess();
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }

    /**
     * 删除好友响应
     *
     * @param messageHolder
     */
    private static void friendRemove(MessageHolder messageHolder) {
        FriendRemoveFutureListener listener = FriendRemove.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            listener.onSuccess();
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }

    /**
     * 查询已添加好友响应
     *
     * @param messageHolder
     * @param myFriend
     */
    private static void allFriend(MessageHolder messageHolder, MyFriend myFriend) {
        AllFriendFutureListener listener = AllFriend.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            listener.onSuccess(myFriend.getFriends());
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }

    /**
     * 修改个人信息
     *
     * @param messageHolder
     */
    private static void updateInfo(MessageHolder messageHolder) {
        InfoUpdateFutureListener listener = InfoUpdate.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            listener.onSuccess();
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }

    /**
     * 查看我的信息响应
     *
     * @param messageHolder
     * @param info
     */
    private static void lookSelfInfo(MessageHolder messageHolder, Info info) {
        SelfInfoFutureListener listener = SelfInfo.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            listener.onSuccess(info.getUsername(), info.getName(), info.getSex(),
                    info.getAge(), info.getPhone(), info.getAddress(), info.getIntroduction());
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }

    /**
     * 查看好友个人信息响应
     *
     * @param messageHolder
     * @param info
     */
    private static void lookFriendInfo(MessageHolder messageHolder, Info info) {
        FriendInfoFutureListener listener = FriendInfo.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            listener.onSuccess(info.getUsername(), info.getName(), info.getSex(),
                    info.getAge(), info.getPhone(), info.getAddress(), info.getIntroduction());
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }

    /**
     * 创建讨论组响应
     *
     * @param messageHolder
     */
    private static void createGroup(MessageHolder messageHolder) {
        GroupCreateFutureListener listener = GroupCreate.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            listener.onSuccess();
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }

    /**
     * 解散讨论组响应
     *
     * @param messageHolder
     */
    private static void disbandGroup(MessageHolder messageHolder) {
        GroupDisbandFutureListener listener = GroupDisband.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            listener.onSuccess();
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }

    /**
     * 添加讨论组成员响应
     *
     * @param messageHolder
     */
    private static void addMember(MessageHolder messageHolder) {
        MemberAddFutureListener listener = MemberAdd.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            listener.onSuccess();
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }

    /**
     * 删除讨论组成员响应
     *
     * @param messageHolder
     */
    private static void removeMember(MessageHolder messageHolder) {
        MemberRemoveFutureListener listener = MemberRemove.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            listener.onSuccess();
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }
}
