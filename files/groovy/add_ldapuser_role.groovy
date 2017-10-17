import groovy.json.JsonSlurper
import org.sonatype.nexus.security.user.UserNotFoundException
import org.sonatype.nexus.security.user.UserSearchCriteria

parsed_args = new JsonSlurper().parseText(args)
try {
   
   //search for a user in LDAP
   criteria = new UserSearchCriteria(userId: parsed_args.usernames, source: "LDAP")
   users = security.securitySystem.searchUsers(criteria)
   //user.forEach { println it }
   users.each {
      if("clevasseur".equalsIgnoreCase(it.userId)){
	log.into("FOUNDED !!!")
      } 
      security.setUserRoles(it.userId, [parsed_args.role])
      security.securitySystem.updateUser(it)
   }
//    security.setUserRoles(parsed_args.usernames, parsed_args.ldap_id, [parsed_args.role] )
} catch(UserNotFoundException e) {
    log.into("User not found ",e)
}
