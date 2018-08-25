$(function () {
  var ChatManager = (function () {
    function ChatManager() {
    }

    ChatManager.textarea = $('#chat-content');
    ChatManager.socket = null;
    ChatManager.stompClient = null;
    ChatManager.sessionId = null;
    ChatManager.chatRoomId = null;
    ChatManager.joinInterval = null;

    ChatManager.join = function () {
      $.ajax({
        url       : 'join',
        headers   : {
          "Content-Type": "application/json"
        },
        beforeSend: function () {
          $('#btnJoin').text('Cancel');
          ChatManager.updateText('waiting anonymous user', false);
          ChatManager.joinInterval = setInterval(function () {
            ChatManager.updateText('.', true);
          }, 1000);
        },
        success   : function (chatResponse) {
          console.log('Success to receive join result. \n', chatResponse);
          if (!chatResponse) {
            return;
          }

          clearInterval(ChatManager.joinInterval);
          if (chatResponse.responseResult == 'SUCCESS') {
            ChatManager.sessionId = chatResponse.sessionId;
            ChatManager.chatRoomId = chatResponse.chatRoomId;
            ChatManager.updateTemplate('chat');
            ChatManager.updateText('>> Connected anonymous user :)\n', false);
            ChatManager.connectAndSubscribe();
          } else if (chatResponse.responseResult == 'CANCEL') {
            ChatManager.updateText('>> Success to cancel', false);
            $('#btnJoin').text('Join');
          } else if (chatResponse.responseResult == 'TIMEOUT') {
            ChatManager.updateText('>> Can`t find user :(', false);
            $('#btnJoin').text('Join');
          }
        },
        error     : function (jqxhr) {
          clearInterval(ChatManager.joinInterval);
          if (jqxhr.status == 503) {
            ChatManager.updateText('\n>>> Failed to connect some user :(\nPlz try again', true);
          } else {
            ChatManager.updateText(jqxhr, true);
          }
          console.log(jqxhr);
        },
        complete  : function () {
          clearInterval(ChatManager.joinInterval);
        }
      })
    };

    ChatManager.cancel = function () {
      $.ajax({
        url     : 'cancel',
        headers : {
          "Content-Type": "application/json"
        },
        success : function () {
          ChatManager.updateText('', false);
        },
        error   : function (jqxhr) {
          console.log(jqxhr);
          alert('Error occur. please refresh');
        },
        complete: function () {
          clearInterval(ChatManager.joinInterval);
        }
      })
    };

    ChatManager.connectAndSubscribe = function () {
      if (ChatManager.stompClient == null || !ChatManager.stompClient.connected) {
        var socket = new SockJS('/chat-websocket');
        ChatManager.stompClient = Stomp.over(socket);
        ChatManager.stompClient.connect({chatRoomId: ChatManager.chatRoomId}, function (frame) {
          console.log('Connected: ' + frame);
          ChatManager.subscribeMessage();
        });
      } else {
        ChatManager.subscribeMessage();
      }
    };

    ChatManager.disconnect = function () {
      if (ChatManager.stompClient !== null) {
        ChatManager.stompClient.disconnect();
        ChatManager.stompClient = null;
        ChatManager.updateTemplate('wait');
      }
    };

    ChatManager.sendMessage = function () {
      console.log('Check.. >>\n', ChatManager.stompClient);
      console.log('send message.. >> ');
      var $chatTarget = $('#chat-message-input');
      var message = $chatTarget.val();
      $chatTarget.val('');

      var payload = {
        messageType    : 'CHAT',
        senderSessionId: ChatManager.sessionId,
        message        : message
      };

      ChatManager.stompClient.send('/app/chat.message/' + ChatManager.chatRoomId, {}, JSON.stringify(payload));
    };

    ChatManager.subscribeMessage = function () {
      ChatManager.stompClient.subscribe('/topic/chat/' + ChatManager.chatRoomId, function (resultObj) {
        console.log('>> success to receive message\n', resultObj.body);
        var result = JSON.parse(resultObj.body);
        var message = '';

        if (result.messageType == 'CHAT') {
          if (result.senderSessionId === ChatManager.sessionId) {
            message += '[Me] : ';
          } else {
            message += '[Anonymous] : ';
          }

          message += result.message + '\n';
        } else if (result.messageType == 'DISCONNECTED') {
          message = '>> Disconnected user :(';
          ChatManager.disconnect();
        }
        ChatManager.updateText(message, true);
      });
    };

    ChatManager.updateTemplate = function (type) {
      var source;
      if (type == 'wait') {
        source = $('#wait-chat-template').html();
      } else if (type == 'chat') {
        source = $('#send-chat-template').html();
      } else {
        console.log('invalid type : ' + type);
        return;
      }
      var template = Handlebars.compile(source);
      var $target = $('#chat-action-div');
      $target.empty();
      $target.append(template({}));
    };

    ChatManager.updateText = function (message, append) {
      if (append) {
        ChatManager.textarea.val(ChatManager.textarea.val() + message);
      } else {
        ChatManager.textarea.val(message);
      }
    };

    return ChatManager;
  }());

  $(document).on('click', '#btnJoin', function () {
    var type = $(this).text();
    if (type == 'Join') {
      ChatManager.join();
    } else if (type == 'Cancel') {
      ChatManager.cancel();
    }
  });

  $(document).on('click', '#btnSend', function () {
    ChatManager.sendMessage();
  });

  ChatManager.updateTemplate('wait');
});