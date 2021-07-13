  $(function () {

    $('.select2').select2();

    $('.select2[multiple]').select2({
      closeOnSelect: false
    });

    $('.select2-tags').select2({
      tags: true,
    });

//    $('.datetime').datetimepicker({
//      format: '',
//    });

    $(".dropdown-menu-input").on("keyup", function () {
      var value = $(this).val().toLowerCase();
      $(".dropdown-menu li").filter(function () {
        $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
      });
    });

  });

  $(document).on('ontouched click', '.autocomplete', function () {
    var text = $(this).data('autocomplete');
    var target = $(this).data('target');
    $('input[name="' + target + '"]').val(text);
  });


