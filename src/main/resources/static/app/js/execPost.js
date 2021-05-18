/**
 * データをPOSTする
 * @param String アクション
 * @param Object POSTデータ連想配列
 * 記述元Webページ http://fujiiyuuki.blogspot.jp/2010/09/formjspost.html
 * サンプルコード
 * <a onclick="execPost('/hoge', {'fuga':'fuga_val', 'piyo':'piyo_val'});return false;" href="#">POST送信</a>
 */
function execPost(action, data) {
    // フォームの生成
    var form = document.createElement("form");
    form.setAttribute("action", action);
    form.setAttribute("method", "post");
    form.style.display = "none";
    document.body.appendChild(form);
    // パラメタの設定
    if (data !== undefined) {
     for (var paramName in data) {
      var input = document.createElement('input');
      input.setAttribute('type', 'hidden');
      input.setAttribute('name', paramName);
      input.setAttribute('value', data[paramName]);
      form.appendChild(input);
     }
    }
    // submit
    form.submit();
   }