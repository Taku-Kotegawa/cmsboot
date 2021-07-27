/**
 * マネージドファイルの添付削除
 * @param {*} element_id <input type="file">のID
 */
function file_detach(element_id) {

  var $upload_file = document.getElementById(element_id);
  $upload_file.style.display = "";
  $upload_file.value = "";
  var attaced_block = document.getElementById(element_id + "__attached-block");
  while (attaced_block.firstChild) {
    attaced_block.removeChild(attaced_block.firstChild);
  }
  // $.ajax({
  //   type: "GET",
  //   url: deleteUrl,
  //   dataType: "json"
  // }).done(function (data) {
  //   console.log("delete temporary file.");
  // });
}

/**
 * マネージドファイルの添付
 * @param {*} element <input type="file">の要素
 */
function file_attach(element) {
  var $upload_file = element;
  var $upload_form = $upload_file.form;
  var $file_type = element.dataset.fileType;

  if (!$upload_file.value) {
    alert("アップロードファイルを指定してください。");
    return;
  }

  if ($upload_file.files[0].size > 100 * 1024 * 1024) {
    alert("ファイルが大きすぎます。(100MBまで)");
    $upload_file.value = "";
    return;
  }

  // サーバにPOSTするフィールド名を強制的に上書き
  $upload_file.name = "file";

  if (window.FormData) {
    var formData = new FormData($upload_form);

    $.ajax({
      type: "POST",
      url: contextPath + "/file?filetype=" + $file_type,
      data: formData,
      enctype: 'multipart/form-data',
      dataType: "json",
      processData: false,
      contentType: false,

    }).done(function (data) { // Ajax通信が成功した時の処理

      if (data.fid == null) {
        alert(data.message);
        return;
      }

      $upload_file.style.display = "none";
      // document.getElementById("upload_file_input-block").style.display = "none";

      var block = document.getElementById($upload_file.id + "__attached-block");
      if (block == undefined) {
        block = document.createElement('div')
        block.id = $upload_file.id + "__attached-block";
        block.classList = "input-group";
        $upload_file.parentElement.insertBefore(block, $upload_file.nextSibling);
      }

      var span = document.createElement('span');

      block.appendChild(span);

      var file_icon = document.createElement('i');
      file_icon.classList = "far fa-file ml-2";
      span.appendChild(file_icon);

//      var link = document.createElement('a');
//      link.href = data.url;
//      link.textContent = data.name;
//      link.target = "_blank";
//      link.classList = "link-attached";
//      span.appendChild(link);

      var filename = document.createElement('span');
      filename.textContent = data.name;
      filename.classList = "link-attached";
      span.appendChild(filename);

      var trush_icon = document.createElement('i');
      trush_icon.classList = "far fa-trash-alt";
      trush_icon.style = "color: brown;";
      trush_icon.onclick = function () {
        file_detach($upload_file.id);
      };
      span.appendChild(trush_icon);

      var uuid = document.createElement('input');
      uuid.type = "hidden";
      uuid.name = $upload_file.id + "Uuid";
      uuid.value = data.uuid;
      span.appendChild(uuid);

      // nameを初期化
      $upload_file.name = "";

    }).fail(function (XMLHttpRequest, textStatus, errorThrown) {
      console.log(XMLHttpRequest);
      console.log(textStatus);
      console.log(errorThrown);
      alert("アップロードが失敗しました。");

      // nameを初期化
      $upload_file.name = "";

    });
  } else {
    alert("アップロードに対応できていないブラウザです。");
  }
}

$(document).ready(function () {
  // ManagedFileのイベント登録
  $('.file-managed').change(function (event) {
    file_attach($(this).get(0))
  });
});
