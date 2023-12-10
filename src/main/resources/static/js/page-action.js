$(document).ready(function() {
	function loadBook(page = null) {
		let data = {
			size: $("#page-size option:selected").val(),
			sortProperty: $("#sort-property option:selected").val(),
			desc: $("#desc option:selected").val(),
			page: 0
		};
		if (page != null) data.page = page;
		let tbBody = $("#book-table tbody");
		$.ajax({
			type: "POST",
			contentType: "application/json; charset=utf-8",
			url: "/getBooks",
			data: JSON.stringify(data),
			cache: false,
			success: function(json) {
				tbBody.html('');
				json.books.forEach((i) => {
					let newRow = $(`<tr>
                    <td><input type="checkbox"></td>
                    <td>${i.upc}</td>
                    <td>${i.price_excl_tax}</td>
                    <td>${i.tax}</td>
                    <td>${i.price}</td>
                    <td>${i.category}</td>
                    <td>${i.availability}</td>
                    <td>${i.num_reviews}</td>
                    <td>${i.stars}</td>
                    <td><a href="${i.url}">Link</a></td>
                </tr>`);
					newRow.data('info', i);
					tbBody.append(newRow);
				});
				let pagination = $('.pagination');
				pagination.html('');
				pagination.append(`<li class=""><button class="page-link" aria-label="Previous"><span aria-hidden="true">«</span></button></li>
							<li class=""><button class="page-link" aria-label="Next"><span aria-hidden="true">»</span></button></li>`)
				let firstPage = pagination.children(':first-child');
				let lastPage = pagination.children(':last-child');
				if (json.number == 0) {
					firstPage.attr('class', 'page-item disabled');
				}
				else {
					firstPage.attr('class', 'page-item');
					firstPage.children('button').click(function() { loadBook() });
				}

				if (json.number == json.totalPages - 1) {
					lastPage.attr('class', 'page-item disabled');
				}
				else {
					lastPage.attr('class', 'page-item');
					lastPage.children('button').click(function() { loadBook(json.totalPages - 1) });
				}
				if (json.totalElements != 0) {
					let count = 0;
					firstPage.after(`<li class="page-item active"><a class="page-link">${json.number + 1}</a></li>`);
					for (let i = 1; (i < 3 || (json.totalPages < i + json.number + 1 && json.totalPages + i - json.number - 1 < 5)) && (json.number + 1 - i) > 0; i++) {
						count++;
						let newElement = $(`<li class="page-item"><button class="page-link">${json.number + 1 - i}</button></li>`);
						firstPage.after(newElement);
						newElement.click(function() { loadBook(json.number - i) });
					}
					for (let i = 1; count < 4 && (json.number + i) < json.totalPages; i++) {
						count++;
						let newElement = $(`<li class="page-item"><button class="page-link">${json.number + 1 + i}</button></li>`);
						lastPage.before(newElement);
						newElement.click(function() { loadBook(json.number + i) });
					}
				}
			}
		});
	};

	$('select').change(function() {
		loadBook();
	});

	function loadPriceStatistics() {
		$.getJSON("/getPriceStatistics", function(json) {
			let tbBody = $("#price-stat tbody");
			tbBody.html('');
			Object.entries(json).forEach(function(entry) {
				const [key, value] = entry;
				let newRow = `<tr>
                                <td>${key}</td>
                                <td>${value.min}</td>
                                <td>${value.max}</td>
                                <td>${(value.sum / value.count).toFixed(2)}</td>
                                <td>${value.count}</td>
                             </tr>`;
				tbBody.append(newRow);
			});
		});
	};

	function loadAvailabilityStatistics() {
		$.getJSON("/getAvailabilityStatistics", function(json) {
			let tbBody = $("#availability-stat tbody");
			tbBody.html('');
			Object.entries(json).forEach(function(entry) {
				const [key, value] = entry;
				let newRow = `<tr>
                                <td>${key}</td>
                                <td>${value.min}</td>
                                <td>${value.max}</td>
                                <td>${value.sum}</td>
                                <td>${(value.sum / value.count).toFixed(2)}</td>
                                <td>${value.count}</td>
                             </tr>`;
				tbBody.append(newRow);
			});
		});
	};

	function loadStarsStatistics() {
		$.getJSON("/getStarsStatistics", function(json) {
			let tbBody = $("#stars-stat tbody");
			tbBody.html('');
			Object.entries(json).forEach(function(entry) {
				const [key, value] = entry;
				let newRow = `<tr>
                                <td>${key}</td>
                                <td>${value.min}</td>
                                <td>${value.max}</td>
                                <td>${(value.sum / value.count).toFixed(2)}</td>
                                <td>${value.count}</td>
                             </tr>`;
				tbBody.append(newRow);
			});
		});
	};

	function loadNumberReviewsStatistics() {
		$.getJSON("/getNumberReviewsStatistics", function(json) {
			let tbBody = $("#num_reviews-stat tbody");
			tbBody.html('');
			Object.entries(json).forEach(function(entry) {
				const [key, value] = entry;
				let newRow = `<tr>
                                <td>${key}</td>
                                <td>${value.min}</td>
                                <td>${value.max}</td>
                                <td>${value.sum}</td>
                                <td>${(value.sum / value.count).toFixed(2)}</td>
                                <td>${value.count}</td>
                             </tr>`;
				tbBody.append(newRow);
			});
		});
	};

	function loadStatistics() {
		loadPriceStatistics();
		loadNumberReviewsStatistics();
		loadStarsStatistics();
		loadAvailabilityStatistics();
	}


	function makeupc(length) {
		let result = '';
		const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
		const charactersLength = characters.length;
		let counter = 0;
		while (counter < length) {
			result += characters.charAt(Math.floor(Math.random() * charactersLength));
			counter += 1;
		}
		return result;
	}

	$('#delete-books').click(function() {
		var books = []
		$("#book-table").find('input[type=checkbox]:checked').each(function() {
			books.push($(this).parent().parent().data('info'));
		});
		console.log(books);
		$.ajax({
			type: "POST",
			contentType: "application/json; charset=utf-8",
			url: "/deleteBooks",
			data: JSON.stringify(books),
			cache: false,
			success: function() {
				$.notify(`Xóa loại sách thành công!`, "success", 9999);
				loadBook();
			},
			error: function() {
				$.notify(`Xóa loại sách thất bại!`, "danger", 9999);
			}
		})
	});
	$("#add-book-modal").on('show.bs.modal', function() {
		$('#upc').val(makeupc(16));
	});
	
	$('#add-book-form').submit(function(event) {
		event.preventDefault();
		
		let formData = {
			upc : $("#upc").val(),
			url : $("#url").val(),
			title : $("#title").val(),
			product_type : "books",
			price_excl_tax : Number($("#price-excl-tax").val()),
			price_incl_tax : Number($("#price-excl-tax").val()) + Number($("#tax").val()),
			tax : Number($("#tax").val()),
			price : Number($("#price").val()),
			availability : Number($("#availability").val()),
			num_reviews : Number($("#num-reviews").val()) ,
			stars : Number($("#stars").val()),
			category : $("#category").val(),
			description : $("#description").val()
		};
		
		$.ajax({
			type: "POST",
			contentType: "application/json; charset=utf-8",
			url: "/addBook",
			data: JSON.stringify(formData),
			cache: false,
			success: function() {
				$.notify("Thêm sách mới thành công!", "success", 9999);
				document.getElementById('add-book-form').reset();
				$('#add-book-modal').modal('hide');
			},
			error: function() {
				$.notify("Thêm sách mới thất bại!", "danger", 9999);
			}
		});
		
	})
	
	loadBook();

	loadStatistics();
});

function isNumber(evt) {
	evt = (evt) ? evt : window.event;
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if ((charCode > 31 && charCode < 48) || charCode > 57) {
		return false;
	}
	return true;
};