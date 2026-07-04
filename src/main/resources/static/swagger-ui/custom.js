document.addEventListener('DOMContentLoaded', function () {
  const observer = new MutationObserver(function () {
    document.querySelectorAll('.info .description').forEach(function (element) {
      if (element && element.textContent && element.textContent.includes('Supported by SmartBear')) {
        element.style.display = 'none';
      }
    });
  });

  if (!document.querySelector('.developer-badge')) {
    const developerBadge = document.createElement('div');
    developerBadge.className = 'developer-badge';
    developerBadge.textContent = 'Developed by Ajeet Kumar';
    document.body.appendChild(developerBadge);
  }

  observer.observe(document.body, { childList: true, subtree: true });
});
