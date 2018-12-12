let pageIndex = 1;
let confirmPassword = ''
let boardUuid = '';
let author = '';
const header = {"Content-Type" : "application/json;charset=UTF-8"};

function getAllList(){
	$.ajax({
		url : `/board/allView.vw/${pageIndex}`,
		method  : 'GET',
		success : function(response){
			
			$(".table").html('<tr> <th> 등록일 </th> <th> 제목 </th> <th> 작성자 </th> </tr>');
			$(".pagination.pagination-primary").html('');
			
			
			var boardAllList = response;
			
			for(myListCompoent of boardAllList.content) {
				$(".table").createListComponent(myListCompoent);
			}
			
			$(".pagination.pagination-primary").createPageLinkGenerator(boardAllList)
		}
	});
}

function initGlobalVariables(){
	confirmPassword ='';
	boardUuid = '';
}

function setPageChanage(pageIndex) {
	this.pageIndex = pageIndex;
	getAllList();
}

function menuSwitching(menu) {
	if(typeof menu !== 'string') throw new Error();
	
	
	if(menu === 'add') {
		$(".btn-view-option").css('display','none');
		$(".btn-add-option").removeAttr('style');
	} else if (menu === 'view') {
		$(".btn-view-option").removeAttr('style');
		$(".btn-add-option").css('display','none');
	}
}

$.fn.createListComponent = function(component){
	if(Boolean(component) === false ) return;
	if(this.is("table") === false) return;
	
	const template = `<tr dataset=${component.boardUuid}>
	<td>
		${component.regTime.replace('T',' ')}
	</td>
	<td>
		<div class="request" data-toggle="modal" data-target="#postView">
			${component.title}
		</div>
	</td>
	<td>
		${component.userName}
	</td>
	</tr>`;
	
	$(this).append(template);
}

$.fn.createPageLinkGenerator = function(pageInfo) {

	let maxGeneratePage = pageInfo.totalPages;
	let currentSelectedPageIndex = pageIndex-1;
	
	for (let i = 0;  i < maxGeneratePage ; i++) {
		let template =  `<li class="page-item">
            <a href="javascript:setPageChanage(${i+1});" class="page-link">${i+1}</a>
           </li>`;
		
		this.append(template);
	}
	
	this.find('li').eq(currentSelectedPageIndex).removeClass('page-item').addClass('active.page-item');
	
	return this;
}

$(document).ready(function(){
	bindEvents();
	getAllList();
});


function bindEvents(){
	$(document).on("click",".request",function(){
		let boardUuid = $(this).closest('tr').attr('dataset');
		
		if(Boolean(boardUuid) === false) return
		
		window.boardUuid = boardUuid;
		
		$.ajax({
			method : "GET",
			url : `/board/detailView.vw?boardUuid=${boardUuid}`,
			success : function(response) {
				var content = response;
				
				menuSwitching('view');
				
				$(".modal-date-block").removeAttr('style');
				$(".modal-titles").val(content.title).css('border','none').attr('readonly','readonly');
				$(".modal-upttime").text(`등록일 : ${content.uptTime.replace('T',' ')}`).css('text-align','right');
				$(".modal-regtime").text(`수정일 : ${content.regTime.replace('T',' ')}`).css('text-align','right');
				$("#modal-author").val(content.userName).css('border','none').attr('readonly','readonly');
				$(".modal-contents").val(content.content).css('border','none').attr('readonly','readonly');
			},
			error : function(response) {
				alert(response);
			}
		});
	});
	
	$(document).on('click',"#savePassword",function(){
		
		window.confirmPassword = $("#password").val();
		
		if(Boolean(window.confirmPassword) === false ){
			alert('비밀번호를 입력해주세요');
			return false;
		}
		
		let method = $(this).attr('proctype');
		let requestData = {'boardUuid' : window.boardUuid, 'password' : window.confirmPassword};

		if(method !== 'DELETE') { // in case of UPDATE POST
			requestData.title = $(".modal-titles").val();
			requestData.content = $(".modal-contents").val();
			requestData.userName = $("#modal-author").val();
		}
		
		$.ajax({
			url : '/board/boardDataManage.do',
			headers : header,
			method : method,
			data : JSON.stringify(requestData),
			success : function(response){
				let serverMessage = response;
				alert(serverMessage.message);
				window.location.reload();
			},
			error : function(response){
				let serverMessage = response.responseJSON;
				alert(serverMessage.message);
				$("#password").val('');
			}
		})
		
	});
	
	$(document).on('click',"#deletePost",function(){
		$("#passwordModal").modal('toggle');
		$("#savePassword").attr('proctype','DELETE');
	});
	
	$(document).on('click',"#updatePost",function(){
		if(verificationParams() === false) {
			alert('게시글의 제목,내용,작성자를 빠짐 없이 입력해주세요.');
			return false;
		}
		
		$("#passwordModal").modal('toggle');
		$("#savePassword").attr('proctype','PUT');
	});
	
	$(document).on('click',"#insertPost", function(){
		
		if(verificationParams() === false) {
			alert('게시글의 제목,내용,작성자를 빠짐 없이 입력해주세요.');
			return false;
		}
		
		$("#passwordModal").modal('toggle');
		$("#savePassword").attr('proctype','POST');
	});
	
	$(document).on('click',"#revisePost",function(){
		$(".modal-titles").removeAttr('readonly').removeAttr('style');
		$(".modal-contents").removeAttr('readonly').removeAttr('style');
		$("#modal-author").removeAttr('readonly').removeAttr('style');
		$(this).attr('id','updatePost').text('저장');
	});
	
	$(document).on('click',"#addPost", function(){
		$("#postView").modal('toggle');
		menuSwitching('add');
		
		$(".modal-date-block").css('display','none');
		
		$(".modal-titles").val('').removeAttr('style').removeAttr('readonly');
		$(".modal-contents").val('').removeAttr('style').removeAttr('readonly');
		$("#modal-author").val('').removeAttr('style').removeAttr('readonly');
	});
	
	$(document).on('click','.btn.btn-danger.btn-link',function(){
		initGlobalVariables();
	});
}

function verificationParams() {
	return Boolean($(".modal-titles").val()) && Boolean($(".modal-contents").val()) && Boolean($("#modal-author").val())
}
