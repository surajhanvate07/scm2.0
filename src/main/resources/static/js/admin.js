document.addEventListener('DOMContentLoaded', function() {
	document.querySelector('#file_input').addEventListener('change', function(event) {
		let fileInput = event.target.files[0];
		if (!fileInput) return;

		let reader = new FileReader();
		reader.onload = function(e) {
			const imgPreview = document.getElementById('upload_image_preview');
			imgPreview.src = reader.result;
			imgPreview.classList.remove('hidden'); // Show the image preview
		};
		reader.readAsDataURL(fileInput);
	});
});
