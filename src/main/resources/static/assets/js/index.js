document.addEventListener("DOMContentLoaded", () => {
    var main = {
        init: function () {
            var _this = this;
            var saveButton = document.getElementById('btn-save');
            if (saveButton) {
                saveButton.addEventListener('click', function () {
                    _this.save();
                });
            }
        },
        save: function () {
            var formData = new FormData(document.getElementById('postForm'));

            console.log('FormData before appending file:', formData);

            // Manually append file if needed
            var fileInput = document.getElementById('content_file');
            if (fileInput && fileInput.files.length > 0) {
                formData.append('file', fileInput.files[0]);
            }

            console.log('FormData after appending file:', formData);

            fetch('/api/save-post', {
                method: 'POST',
                body: formData,
            }).then(function (response) {
                if (response.status == 200) {
                    alert('글이 등록되었습니다.');
                    window.location.href = '/';
                } else {
                    alert('글 등록에 실패했습니다.');
                }
            });
        },
    };

    main.init();
});