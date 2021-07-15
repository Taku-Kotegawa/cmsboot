function triggerEvent(element, event) {
    if (document.createEvent) {
        // IE以外
        var evt = document.createEvent("HTMLEvents");
        evt.initEvent(event, true, true ); // event type, bubbling, cancelable
        return element.dispatchEvent(evt);
    } else {
        // IE
        var evt = document.createEventObject();
        return element.fireEvent("on"+event, evt)
    }
 }

  /**
   * Ajax により Action へリクエストを発行し、対象となるセレクトボックスのリストを変更します。<br />
   * この関数は、あるセレクトボックスで項目が選択された際に、選択された値に応じて関連するセレクトボックスの内容を動的に(非同期)変更したい場合に使用します。<br />
   * Action クラス側で、以下の実装が必要になります。
   * <ul>
   * <li>選択値を、リクエストパラメータ名 "selected_value_" で取得する。</li>
   * <li>セレクトボックスのデータは、JSON 形式で返す。</li>
   * <li>JSON データは、sqlSelectタグと同様に、ラベルを"LABEL"、コードを"VALUE" とした一覧形式で作成する。</li>
   * </ul>
   *
   * @param {String} action リクエスト先の Action 名
   * @param {String} value 選択値
   * @param {String} targetId 対象となるプルダウン要素のid属性値
   * @param {Boolean} empty リストの先頭項目に空白項目を挿入するかどうかのフラグ
   * @param {Boolean} selected 対象プルダウンで同じ値がある場合に選択状態にするかどうかのフラグ
   * @example // イベント発生元 &lt;select id="item_select1"name="_.item_select1"
   *          onchange="St.ui.ajaxSelectList('/sample/v00101_01.action',
   *          this.value, 'item_select2')"&gt; // 変更対象 &lt;select
   *          id="item_select2" name="_.item_select2"&gt;
   */
  var ajaxSelectList = function (url, value, targetId, empty, selected) {
    //   var url = St.c.getActionUrl(action);
      var data = "selected_value=" + value;
      var success = function (response) {
          updateSelectList(targetId, response, empty, selected);
      }
      var error = function (xhr, status, err) {
          alert("[error] ajaxSelectList");
      }
      sendAjaxRequest(url, data, success, error);
  };


  /**
   * 指定されたセレクトボックスのリストを変更します。<br />
   * JSON データには、sqlSelectタグと同様に、ラベルを"LABEL"、コードを"VALUE" とした一覧形式のデータを指定する。
   *
   * @param {String} targetId 対象となるプルダウン要素のid属性値
   * @param {Object} data 設定するJSONデータ
   * @param {Boolean} empty リストの先頭項目に空白項目を挿入するかどうかのフラグ
   * @param {Boolean} selected 対象プルダウンで同じ値がある場合に選択状態にするかどうかのフラグ
   */
  var updateSelectList = function (targetId, data, empty, selected) {
      var target = document.getElementById(targetId);
      var currentValue = target.value;
      var option, val, lab;
      target.innerHTML = "";
      if (empty) {
          var elem = document.createElement("option");
          elem.value = "";
          target.appendChild(elem);
      }
      for (var i in data) {
          option = document.createElement("option");
          val = data[i].value;
          lab = data[i].label;
          option.value = typeof val === "undefined" ? "" : val;
          option.textContent = typeof lab === "undefined" ? "" : lab;
          if (selected && val == currentValue) {
              option.selected = true;
          }
          target.appendChild(option);
      }
      triggerEvent(target, "change");
  };


  /**
   * 指定されたデータを、非同期で送信します。
   *
   * @param {String} url 送信先のURL
   * @param {String} data 送信するデータ
   * @param {Function} success 通信成功時のコールバック関数
   * @param {Function} error 通信エラー時のコールバック関数
   *
   */
  var sendAjaxRequest = function (url, data, success, error) {
      var xhr = new XMLHttpRequest();
      xhr.onreadystatechange = function () {
          if (xhr.readyState === 4) {
              var statusCode = xhr.status;
              if (200 <= statusCode && statusCode < 300) {
                  var response = JSON.parse(xhr.responseText);
                  success(response, statusCode);
              } else {
                  var statusText = xhr.statusText;
                  var err = xhr.statusText;
                  if (statusCode || !statusText) {
                      statusText = "error";
                  }
                  error(xhr, statusText, err);
              }
          }
      };
      xhr.open("GET", url + "?" + data, true);
    //   xhr.setRequestHeader("Content-Type",
    //       "application/x-www-form-urlencoded");
    //   xhr.dataType = "json";
      try {
          xhr.send(data);
      } catch (e) {
          xhr.abort();
      }
  }


  /**
   * コンテキストパスの値を返します。 ルートコンテキストの場合は、空文字("")を返します。
   *
   * @return コンテキストパス
   * @example var contextPath = St.c.getContextPath(); // contextPath :
   *          "/sample" ([sample]システムの場合)
   */
  var getContextPath = function () {
      var jsPath = "/stc/js/st.base.js";
      var selector = "head > script[src*='" + jsPath + "']";
      var obj = document.querySelector(selector);
      var src = obj.getAttribute("src");
      var index = src.indexOf(jsPath);
      if (index > 0) {
          return src.substring(0, index);
      } else {
          return "";
      }
  };

  /**
   * 指定された Action を呼び出す URL を返します。
   *
   * @param {String} action Action 名
   * @return {String} Action URL
   * @example var actionUrl = St.c.getActionUrl("v00101"); // actionUrl :
   *          "/コンテストパス/v00101.action"
   */
  var getActionUrl = function (action) {
      var path = getContextPath();
      if (action.charAt(0) != "/") {
          path = path + "/";
      }
      return path + action + ".action";
  };