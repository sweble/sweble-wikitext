#!/usr/bin/php
<?php
/*
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This script takes a FILE and a TITLE and passes both to the API of the
 * English Wikipedia to retrieve an expanded version of the Wikitext found
 * in the file. The API will pretend that the Wikitext comes from a page
 * with the name TITLE. The result is printed to STDOUT.
 */

	if (count($argv) != 3)
	{
		fwrite(STDERR, "Usage: $argv[0] FILE TITLE\n");
		exit(1);
	}

	include('HTTP/Request.php');

	$req = &new HTTP_Request('http://en.wikipedia.org/w/api.php');
	$req->setMethod(HTTP_REQUEST_METHOD_POST);

	$req->addPostData('action', 'expandtemplates');
	$req->addPostData('format', 'php');
	$req->addPostData('title', $argv[2]);
	$req->addPostData('text', file_get_contents($argv[1]));

	$response = $req->sendRequest();
	$expanded = unserialize($req->getResponseBody());
	print_r(current($expanded['expandtemplates']));

?>
