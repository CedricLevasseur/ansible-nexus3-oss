import groovy.json.JsonSlurper
import org.sonatype.nexus.security.user.UserNotFoundException

parsed_args = new JsonSlurper().parseText(args)

try {
    // update an existing user
    security.setUserRoles(parsed_args.usernames, parsed_args.ldap_id parsed_args.role)
} catch(UserNotFoundException ignored) {
    //nothing to do 
}
