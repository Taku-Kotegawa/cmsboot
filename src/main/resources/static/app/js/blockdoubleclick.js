/**
 * フォーム画面でのダブルクリックによる２重送信防止
 * submit後のsubmitボタンやアンカーのクリックを無効化する。
 */

// コンテンツが完全にロードした後に各種イベントを追加
document.addEventListener('DOMContentLoaded', function () {

  // <form>のsubmitイベントに処理追加
  var forms = document.querySelectorAll('form');
//  forms.forEach(form => {
//    form.addEventListener('submit', setClicked);
//  });

  Array.prototype.forEach.call(forms, function(form) {
    form.addEventListener('submit', setClicked);
  });


  // submitタイプの要素のclickイベントに処理追加
  var submits = document.querySelectorAll('[type="submit"]')
//  submits.forEach(e => {
//    e.addEventListener('click', blockDoubleDlick);
//  })
  Array.prototype.forEach.call(submits, function(e){
    e.addEventListener('click', blockDoubleDlick);
  })


  // アンカーのclickイベントに処理追加
  var anchors = document.querySelectorAll('a');
//  anchors.forEach(e => {
//    e.addEventListener('click', blockDoubleDlick);
//  })
  Array.prototype.forEach.call(anchors, function(e){
    e.addEventListener('click', blockDoubleDlick);
  })

  // class="submit-confirm" で確認ダイアログ表示
  var submits = document.querySelectorAll('.submit-confirm')
//  submits.forEach(e => {
//    e.addEventListener('click', submitConfirm);
//  })
  Array.prototype.forEach.call(submits, function(e){
    e.addEventListener('click', submitConfirm);
  })

});


/**
 * Submitを行う要素にdata-clicked属性を追加
 * @param {*} event
 */
function setClicked(event) {

  var submits = document.querySelectorAll('[type="submit"]')
//  submits.forEach(e => {
//    e.dataset.clicked = 'clicked';
//  })
  Array.prototype.forEach.call(submits, function(e){
    e.dataset.clicked = 'clicked';
  })

  var anchors = document.querySelectorAll('a');
//  anchors.forEach(e => {
//    e.dataset.clicked = 'clicked';
//  })
  Array.prototype.forEach.call(anchors, function(e){
    e.dataset.clicked = 'clicked';
  })

  // setTimeout(function(){unsetClicked()}, 30000); //30秒後に解除

  // 押下したボタンのみを対象にclicked属性を設定する(旧バージョン)
  // event.submitter.dataset.clicked = 'clicked'
  // setTimeout(function(){event.submitter.dataset.clicked = ''}, 30000); //30秒後に解除
  // console.log('will clear data-clicked after 30sec.');

  console.log('submit. set data-clicked = clicked');

}

/**
 * data-clicked属性の解除
 */
function unsetClicked() {
//  document.querySelectorAll('[type="submit"]').forEach(e => {
//    e.dataset.clicked = '';
//  })
    Array.prototype.forEach.call(document.querySelectorAll('[type="submit"]'), function(e){
    e.dataset.clicked = '';
  })

//  document.querySelectorAll('a').forEach(e => {
//    e.dataset.clicked = '';
//  })
  Array.prototype.forEach.call(document.querySelectorAll('a'), function(e){
    e.dataset.clicked = '';
  })

  console.log('unset data-clicked');
}

/**
 * Submit後のクリックをキャンセル
 * @param {*} event
 */
function blockDoubleDlick(event) {
  if (event.currentTarget.dataset.clicked == 'clicked') {
    console.log('block double submit.')
    event.preventDefault();
  }
}

/**
 * Submit確認ダイアログ表示
 * @param {*} event
 */
function submitConfirm(event) {
  if (!window.confirm("サーバにデータを送信します。よろしいですか？")) {
    event.preventDefault();
  }
}
