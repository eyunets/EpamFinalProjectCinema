$(document).ready(function() {

	$('.reserve-film').click(function() {
		reserveFilm($(this));
	});
	$('.ban-user').click(function() {
		banUser($(this));
	});
});


function banUser(element) {
	var userID = $(element).attr('id');
	$.ajax(
			{
				type : 'get',
				url : contextUrl + '/frontController?command=banUser&userID='
						+ userID,
				success : function(response) {
					var str = $('.ban-user#' + userID).text().trim();
					if (str === ban) {
						console.log(str + ban);
						$('.ban-user#' + userID).removeClass('btn-danger')
								.addClass('btn-success').text(unban);
					} else {
						console.log(str + unban);
						$('.ban-user#' + userID).removeClass('btn-success')
								.addClass('btn-danger').text(ban);
					}

				}

			}).fail(function(data) {
		if (console && console.log) {
			console.log("Error data:", data);
		}
	});
}
