package org.gern.birdfeeder.testsupport

import org.mariadb.jdbc.MariaDbDataSource

fun testDataSource() = MariaDbDataSource("jdbc:mysql://localhost:3306/feed_test?user=birdfeeder")
