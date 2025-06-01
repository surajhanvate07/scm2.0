const viewContactModal = document.getElementById("view_contact_modal");

const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

const contactModal = new Modal(viewContactModal, options, instanceOptions);

function openContactModal() {
	contactModal.show();
}

function closeContactModal() {
	contactModal.hide();
}

async function loadContactData(contactId) {
	console.log("Loading contact data for ID:", contactId);

//	fetch(`/api/contacts/${contactId}`)
//		.then((response) => response.json())
//		.then((data) => {
//			console.log("Contact data fetched:", data);
//		})
//		.catch((error) => {
//			console.error("Error fetching contact data:", error);
//		});

	try {
    	const data = await (await fetch(`/api/contacts/${contactId}`)).json();
		document.getElementById("contact_name").textContent = data.name;
		document.getElementById("contact_email").textContent = data.email;
		document.getElementById("contact_phone").textContent = data.phoneNumber;
		document.getElementById("contact_image").src = data.picture;
		document.getElementById("contact_address").textContent = data.address;
		const aboutDiv = document.getElementById("contact_about_wrapper");
        if (data.description) {
        	document.getElementById("contact_about").textContent = data.description;
        	aboutDiv.classList.remove("hidden");
        } else {
        	aboutDiv.classList.add("hidden");
        }
		document.getElementById("contact_favourite").textContent= data.favorite ? "Yes" : "No";
        const websiteDiv = document.getElementById("website_link_wrapper");
        if(data.websiteLink) {
        	document.getElementById("contact_website").href = data.websiteLink;
        	document.getElementById("contact_website").textContent = data.websiteLink;
			websiteDiv.classList.remove("hidden");
		} else {
			websiteDiv.classList.add("hidden");
		}
		 const linkedInDiv = document.getElementById("linkedIn_link_wrapper");
		 if(data.linkedInLink) {
		 	document.getElementById("contact_linkedIn").href = data.linkedInLink;
		 	document.getElementById("contact_linkedIn").textContent = data.linkedInLink;
		 	linkedInDiv.classList.remove("hidden");
		 } else {
		 	linkedInDiv.classList.add("hidden");
		 }
    	openContactModal();
    } catch (error) {
    	console.error("Error fetching contact data:", error);
    }
}

async function deleteContact(contactId) {
  const isDarkMode = document.documentElement.classList.contains("dark");

  Swal.fire({
    title: "Do you want to delete the contact?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "Delete",
    confirmButtonColor: "#e3342f", // red-600
    cancelButtonColor: "#6b7280",  // gray-500
    background: isDarkMode ? "#1f2937" : "#ffffff", // gray-800 or white
    color: isDarkMode ? "#e5e7eb" : "#111827",       // gray-200 or gray-900
    customClass: {
      popup: 'rounded-lg shadow-lg',
      confirmButton: 'focus:outline-none',
      cancelButton: 'focus:outline-none',
    }
  }).then(async (result) => {
    if (result.isConfirmed) {
    	const url = "/user/contacts/delete/" + contactId;
    	window.location.replace(url);
    }
  });
}

