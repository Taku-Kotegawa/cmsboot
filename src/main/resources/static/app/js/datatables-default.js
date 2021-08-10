// DataTables標準の要素にClassを追加
$.extend($.fn.dataTableExt.oStdClasses, {
    "sFilterInput": "form-control ml-0 mr-4",
    "sLengthSelect": "form-control",
});

// DataTablesの共通設定
$.extend($.fn.dataTable.defaults, {

    // サーバサイド処理を有効
    'serverSide': true,

    // 遅延読込
    // "deferRender": true,

    // 処理中の表示
    'processing': false,

    // データ取得先のURLの初期値設定
    // flattenはSpringMVCでデータを受け取るためのクエリパラメータの変換処理
//    'ajax': {
//        'url': 'list/json',
//        'data': flatten
//    },

    // 検索条件・並び順などの状態をLocalStorageに保存し、再表示時に復元
    'stateSave': true,
    'stateDuration': -1,

    // グローバルフィルタ、ページ数切替、ページネーションボタン等の部品のレイアウトを調整
    'dom':"<'row'<'col-32 d-inline-flex'fB><'col-4 text-right'l>>" +
        "<'row'<'col-36'tr>>" +
        "<'row'<'col-15'i><'col-21'p>>",

    // 日本語化
    'language': {
        'sEmptyTable': 'テーブルにデータがありません',
        'sInfo': ' _TOTAL_ 件中 _START_ から _END_ まで表示',
        'sInfoEmpty': ' 0 件中 0 から 0 まで表示',
        'sInfoFiltered': '',
        'sInfoPostFix': '',
        'sInfoThousands': ',',
        'sLengthMenu': '_MENU_',
        'sLoadingRecords': '読み込み中...',
        'sProcessing': '処理中...',
        'sSearch': '',
        'sZeroRecords': '一致するレコードがありません',
        'oPaginate': {
            'sFirst': '<i class="fa fa-angle-double-left"></i>',
            'sLast': '<i class="fa fa-angle-double-right"></i>',
            'sNext': '<i class="fa fa-angle-right"></i>',
            'sPrevious': '<i class="fa fa-angle-left"></i>'
        },
        'oAria': {
            'sSortAscending': ': 列を昇順に並べ替えるにはアクティブにする',
            'sSortDescending': ': 列を降順に並べ替えるにはアクティブにする'
        },
        // ボタンに名前(フォントアイコン)を割り当てる
        'buttons': {
            'colvis': '<i class="fas fa-filter"></i>',
            'copy': '<i class="fa fa-copy fa-fw"></i>',
            'excel': '<i class="fa fa-file-excel-o fa-fw"></i>'
        },
    },
    // 列順変更許可
    'colReorder': true,

    // テーブル幅自動設定
    "autoWidth": false,

});


/**
 * 項目単位フィルタを追加する共通処理
 * @param {*} table
 */
function addFieldFilter(table) {

    // 項目単位フィルタのためのイベント処理を設定する。
    $('input.dataTables_column_filter').on('keyup', function (e, s) {
        if (e.which == 13 || this.value.length == 0) {
            var idx = $(this).attr('data-column');
            table.column(idx).search(this.value).draw();
            //            fnRecoverFieldSearch(table);
        }
    });

    // 画面リロード時の復元
    fnRecoverFieldSearch(table);
}

/**
 * StateSave利用時に項目単位フィルタの検索値を検索フィールドに復元、StateSave=falseの場合は不要
 * @param {*} table
 */
function fnRecoverFieldSearch(table) {
    table.columns().every(function () {
        var idx = this.index();
        var str = this.search();
        if (str != undefined) {

            var element = document.getElementById('col_filter_' + idx);
            // Selectの場合
            if (element != undefined && element.length != undefined) {
                var array = this.search().split(',');
                $('#col_filter_' + idx).val(array);
            } else {
                // Inputの場合
                $('#col_filter_' + idx).val(str);
            }
        }
    });
}

/**
 * 項目単位フィルタを追加する共通処理(列の並び順対応版)
 */
function addFieldFilter2(table) {

    // 項目単位フィルタのためのイベント処理を設定する。
    $('input.dataTables_column_filter').on('keyup', function (e, s) {
        if (e.which == 13 || this.value.length == 0) {
            var th = $(this).parents('th')[0];
            var visIndex = th.cellIndex;
            table.column(visIndex + ':visIdx').search(this.value).draw();
        }
    });

    // 項目単位フィルタのためのイベント処理を設定する。(Select用)
    $('select.dataTables_column_filter').on('change', function (e, s) {
        var th = $(this).parents('th')[0];
        var visIndex = th.cellIndex;
        var value = "";
        for (var i = 0; i < this.length; i++) {
            if (this[i].selected) {
                if (value != "") {
                    value += ","
                }
                value += this[i].value;
            }
        }
        table.column(visIndex + ':visIdx').search(value).draw();
    });

    fnRecoverFieldSearch(table);
    //    restoreColumnFilterByColReOrder(table);


    // fixedColumns 上の検索項目はレンダリングのタイムラグがある
    setTimeout(
        function () {
          $('.DTFC_LeftWrapper input.dataTables_column_filter').on('keyup', function (e, s) {
            if (e.which == 13 || this.value.length == 0) {
                var th = $(this).parents('th')[0];
                var visIndex = th.cellIndex;
                table.column(visIndex + ':visIdx').search(this.value).draw();
            }
          });
        }, 
        "3000"
        );

}


//列指定検索テキストボックスの値復元（ColReorder対応）
// function restoreColumnFilterByColReOrder(table) {

//     // 列の並び順を控えていく
//     var colOrder = table.colReorder.order();

//     // DataTablesで定義されている列数分だけループする
//     table.columns().every(function () {
//         var data = this.data();
//         var idx = this.index();
//         var str = table.columns(idx).search()[0];
//         $('#col' + colOrder[idx] + '_filter').val(str);
//     });
// }



// DataTables で列検索を行い再描画する（ColReorder対応）
// function filterColumnByColReOrder(table, col) {
//     var th = $(col).parents('th')[0];
//     var visIndex = th.cellIndex;
//     var str = $(col).val();
//     table.column(visIndex + ':visIdx').search(str).draw();
// }


/**
 * グローバルフィルターでEnterキーで送信
 * @param {*} table
 */
function fnGlobalFilterOnReturn(table) {
    $('.dataTables_filter input').unbind();
    $('.dataTables_filter input').bind('keyup', function (e) {
        if (e.which == 13 || this.value.length == 0) {
            table.search(this.value).draw();
        }
    });
}


/**
 * DataTables Ajaxクエリの修正
 * @param {*} params
 */
function flatten(params, settings) {

    params.columns.forEach(function (column, index) {
        params['columns[' + index + '].data'] = column.data;
        params['columns[' + index + '].name'] = column.name;
        params['columns[' + index + '].searchable'] = column.searchable;
        params['columns[' + index + '].orderable'] = column.orderable;
        params['columns[' + index + '].search.regex'] = column.search.regex;
        params['columns[' + index + '].search.value'] = column.search.value;
    });
    delete params.columns;

    params.order.forEach(function (order, index) {
        params['order[' + index + '].column'] = order.column;
        params['order[' + index + '].dir'] = order.dir;
    });
    delete params.order;

    params['search.regex'] = params.search.regex;
    params['search.value'] = params.search.value;
    delete params.search;

    return params;
}


/**
 * 状態クリアボタン
 */
$.fn.dataTable.ext.buttons.stateClear = {
    text: '<i class="fas fa-redo"></i>',
    titleAttr: '状態クリア',
    action: function (e, dt, node, config) {
        dt.state.clear();
        window.location.reload();
    }
};

/**
 * 再検索ボタン
 */
$.fn.dataTable.ext.buttons.refresh = {
    text: '<i class="fa fa-search fa-fw" aria-hidden="true"></i>',
    titleAttr: '再検索',
    action: function (e, dt, node, config) {
        dt.ajax.reload();
    }
};

/**
 * 新規作成ボタン
 */
$.fn.dataTable.ext.buttons.createnew = {
    text: '<i class="far fa-file"></i>',
    titleAttr: '新規作成',
    action: function (e, dt, node, config) {
        window.location.href = "create?form";
    }
};

/**
 * CSVダウンロード(サーバサイド処理)
 */
$.fn.dataTable.ext.buttons.csvdownload = {
    text: '<i class="fas fa-file-download"></i> C',
    titleAttr: 'CSVダウンロード',
    action: function (e, dt, node, config) {
        var data = dt.ajax.params();
        window.open('list/csv?' + $.param(data), '_blank');
    }
};

/**
 * CSVダウンロード(サーバサイド処理)
 */
$.fn.dataTable.ext.buttons.tsvdownload = {
    text: '<i class="fas fa-file-download"></i> T',
    titleAttr: 'TSVダウンロード',
    action: function (e, dt, node, config) {
        var data = dt.ajax.params();
        window.open('list/tsv?' + $.param(data), '_blank');
    }
};

/**
 * CSVダウンロード(サーバサイド処理)
 */
$.fn.dataTable.ext.buttons.exceldownload = {
    text: '<i class="fas fa-file-download"></i> E',
    titleAttr: 'Excelダウンロード',
    action: function (e, dt, node, config) {
        var data = dt.ajax.params();
        window.open('list/excel?' + $.param(data), '_blank');
    }
};

/**
 * アップロード画面ボタン
 */
$.fn.dataTable.ext.buttons.upload = {
    text: '<i class="fa fa-upload" aria-hidden="true"></i>',
    titleAttr: 'アップロード',
    action: function (e, dt, node, config) {
        window.location.href = "upload?form";
    }
};

function postBulkOperation(operation, data) {
    let form = document.getElementById('bulk-operation-form');
    form.action = operation;

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

function confirmbulkOperation(dt, operation, message) {
    const formatter = new Intl.NumberFormat('ja-JP');
    let data = {};
    let selectedKey = getSelectedKey(dt, 0);
    data.selectedKey = selectedKey.join(',');
    let replacedMessage = message.replace('%i', formatter.format(selectedKey.length));

    if (selectedKey.length == 0) {
        alert('データが選択されていません。');
        return false;
    }

    if(confirm(replacedMessage)){
        postBulkOperation(operation, data);
    }
}


/**
 * 一括操作(削除)
 */
$.fn.dataTable.ext.buttons.bulkdelete = {
    text: '<i class="fa fa-trash" aria-hidden="true"></i> *',
    titleAttr: '選択された無効データの一括削除',
    action: function (e, dt, node, config) {
        confirmbulkOperation(dt, 'bulk_delete', '選択された %i 件のデータのうち、無効なデータを削除します。\n(ステータスが「無効」以外はスキップされます)');
        dt.columns().checkboxes.deselectAll();
    }
};


/**
 * 一括操作(無効化)
 */
$.fn.dataTable.ext.buttons.bulkinvalid = {
    text: '<i class="fa fa-ban" aria-hidden="true"></i> *',
    titleAttr: '選択された有効データの無効化',
    action: function (e, dt, node, config) {
        confirmbulkOperation(dt, 'bulk_invalid', '選択された %i 件のデータのうち、有効なデータを無効にします。\n(ステータスが「有効」以外はスキップされます)');
    }
};

/**
 * 一括操作(有効化)
 */
$.fn.dataTable.ext.buttons.bulkvalid = {
    text: '<i class="fa fa-check-square" aria-hidden="true"></i> *',
    titleAttr: '選択された無効データの有効化',
    action: function (e, dt, node, config) {
        confirmbulkOperation(dt, 'bulk_valid', '選択された %i 件のデータのうち、無効なデータを有効にします。\n(ステータスが「無効」以外はスキップされます)');
    }
};

/**
 * checkboxesで選択されている行のキーの一覧を取得する
 */
function getSelectedKey(table, checkboxesColumnNo) {
    let rowsSelected = table.column(checkboxesColumnNo).checkboxes.selected()
    let checked = [];
    for (var i = 0; i < rowsSelected.length; i++) {
        checked.push(rowsSelected[i]);
    }
    return checked;
}