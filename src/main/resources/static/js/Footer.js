import React from 'react';

const Footer = () => {
  return (
    <footer style={styles.footer}>
      <div style={styles.container}>
        <h3 style={styles.heading}>DriveX Car Rentals</h3>
        <p style={styles.tagline}>Premium vehicles for your next adventure.</p>
        
        <div style={styles.contactInfo}>
          <p>📍 Bengaluru, Karnataka</p>
          <p>👤 <strong>Manas Kumar</strong></p>
          <p>📞 <a href="tel:+917061356710" style={styles.link}>+91 706 135 6710</a></p>
          <p>✉️ <a href="mailto:kaustuk2003@gmail.com" style={styles.link}>kaustuk2003@gmail.com</a></p>
        </div>
        
        <p style={styles.copyright}>© 2026 DriveX. All rights reserved.</p>
      </div>
    </footer>
  );
};

const styles = {
  footer: {
    backgroundColor: '#1a1a1a',
    color: '#ffffff',
    padding: '40px 20px',
    marginTop: '50px',
    textAlign: 'center',
    fontFamily: 'Arial, sans-serif'
  },
  container: {
    maxWidth: '800px',
    margin: '0 auto',
  },
  heading: {
    margin: '0 0 10px 0',
    fontSize: '24px'
  },
  tagline: {
    color: '#a0a0a0',
    marginBottom: '20px'
  },
  contactInfo: {
    margin: '20px 0',
    lineHeight: '1.8',
    fontSize: '16px'
  },
  link: {
    color: '#4da6ff',
    textDecoration: 'none'
  },
  copyright: {
    color: '#666666',
    fontSize: '14px',
    marginTop: '30px'
  }
};

export default Footer;
