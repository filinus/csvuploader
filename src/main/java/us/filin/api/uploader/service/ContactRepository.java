
package us.filin.api.uploader.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.filin.api.uploader.jpa.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
}
