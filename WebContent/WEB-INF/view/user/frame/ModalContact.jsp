<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="modal fade" id="contact_modal" tabindex="-1" role="dialog"
	aria-labelledby="contactLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="contactLabel">Contact</h4>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>

			</div>
			<form action="/contactSend.do" class="" onsubmit="return check()"
				method="post">
				<div class="modal-body">
					<div class="col-md-10" style="margin: auto;">
						<div class="control-group form-group">
							<div class="controls">
								<label>Full Name:</label> <input class="form-control"
									name="contactName" required type="text">
								<p class="help-block"></p>
							</div>
						</div>
						<div class="control-group form-group">
							<div class="controls">
								<label>Phone Number:</label> <input class="form-control"
									name="contactTel" required type="tel">
							</div>
						</div>
						<div class="control-group form-group">
							<div class="controls">
								<label>Email Address:</label> <input class="form-control"
									name="contactEmail" required type="email">
							</div>
						</div>
						<div class="control-group form-group">
							<div class="controls">
								<label>Message:</label>
								<textarea class="form-control" cols="100" name="contactMessage"
									maxlength="999" required rows="10" style="resize: none"></textarea>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="submit" class="btn btn-primary" style="color: dodgerblue;">Send Message</button>
				</div>
			</form>
		</div>
	</div>
</div>
